/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.game;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import com.craftingdead.core.network.Synched;
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
  private final Map<IRegistryDelegate<ModuleType>, Synched> networkModules;

  public GameWrapper(T game) {
    this.game = game;
    ImmutableMap.Builder<IRegistryDelegate<ModuleType>, M> builder = ImmutableMap.builder();
    ImmutableList.Builder<Module.Tickable> tickableBuilder = ImmutableList.builder();
    ImmutableMap.Builder<IRegistryDelegate<ModuleType>, Synched> networkBuilder =
        ImmutableMap.builder();
    this.game.registerModules(module -> {
      builder.put(module.getType().delegate, module);
      if (module instanceof Module.Tickable) {
        tickableBuilder.add((Module.Tickable) module);
      }
      if (module instanceof Synched) {
        networkBuilder.put(module.getType().delegate, (Synched) module);
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
    for (Map.Entry<IRegistryDelegate<ModuleType>, Synched> entry : this.networkModules
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
      Synched module = this.networkModules.get(moduleType.delegate);
      if (module == null) {
        throw new IllegalStateException(
            "Module not present with ID: " + moduleType.getRegistryName().toString());
      }
      module.decode(in);
    }
  }

  protected boolean requiresSync() {
    return this.game.requiresSync()
        || this.networkModules.values().stream().anyMatch(Synched::requiresSync);
  }
}
