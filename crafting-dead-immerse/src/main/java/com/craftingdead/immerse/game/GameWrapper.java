package com.craftingdead.immerse.game;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.immerse.game.module.Module;
import com.craftingdead.immerse.game.module.ModuleType;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.SyncGameMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.Unpooled;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.IRegistryDelegate;

public class GameWrapper<T extends Game<M>, M extends Module> {

  private final T game;

  protected final Map<IRegistryDelegate<ModuleType>, M> modules;
  private final Collection<Module.Tickable> tickableModules;
  private final Map<IRegistryDelegate<ModuleType>, BufferSerializable> networkModules;

  public GameWrapper(T game) {
    this.game = game;
    ImmutableMap.Builder<IRegistryDelegate<ModuleType>, M> builder = ImmutableMap.builder();
    ImmutableList.Builder<Module.Tickable> tickableBuilder = ImmutableList.builder();
    ImmutableMap.Builder<IRegistryDelegate<ModuleType>, BufferSerializable> networkBuilder =
        ImmutableMap.builder();
    this.game.registerModules(module -> {
      builder.put(module.getType().delegate, module);
      if (module instanceof Module.Tickable) {
        tickableBuilder.add((Module.Tickable) module);
      }
      if (module instanceof BufferSerializable) {
        networkBuilder.put(module.getType().delegate, (BufferSerializable) module);
      }
    });
    this.modules = builder.build();
    this.tickableModules = tickableBuilder.build();
    this.networkModules = networkBuilder.build();
  }

  public T getGame() {
    return this.game;
  }

  @Nullable
  public M getModule(ModuleType moduleType) {
    return this.modules.get(moduleType.delegate);
  }

  public void load() {
    this.modules.values().forEach(Module::load);
    this.game.load();
  }

  public void unload() {
    this.game.unload();
    this.modules.values().forEach(Module::unload);
  }

  public void tick() {
    for (Module.Tickable module : this.tickableModules) {
      module.tick();
    }
    this.game.tick();
  }

  public IPacket<?> buildSyncPacket(boolean writeAll) {
    PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
    this.encode(packetBuffer, writeAll);
    return NetworkChannel.PLAY.getSimpleChannel()
        .toVanillaPacket(new SyncGameMessage(packetBuffer), NetworkDirection.PLAY_TO_CLIENT);
  }

  private void encode(PacketBuffer out, boolean writeAll) {
    this.game.encode(out, writeAll);

    out.writeVarInt(this.modules.size());
    for (Map.Entry<IRegistryDelegate<ModuleType>, BufferSerializable> entry : this.networkModules
        .entrySet()) {
      out.writeRegistryId(entry.getKey().get());
      entry.getValue().encode(out, writeAll);
    }
  }

  public void decode(PacketBuffer in) {
    this.game.decode(in);

    int size = in.readVarInt();
    for (int i = 0; i < size; i++) {
      ModuleType moduleType = in.readRegistryIdSafe(ModuleType.class);
      BufferSerializable module = this.networkModules.get(moduleType.delegate);
      if (module == null) {
        throw new IllegalStateException(
            "Module not present with ID: " + moduleType.getRegistryName().toString());
      }
      module.decode(in);
    }
  }

  protected boolean requiresSync() {
    return this.game.requiresSync()
        || this.networkModules.values().stream().anyMatch(BufferSerializable::requiresSync);
  }
}
