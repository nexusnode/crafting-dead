/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.game;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import com.craftingdead.core.network.Synched;
import com.craftingdead.immerse.game.module.GameModule;
import com.craftingdead.immerse.game.module.ModuleType;
import com.craftingdead.immerse.network.NetworkChannel;
import com.craftingdead.immerse.network.play.SyncGameMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.registries.IRegistryDelegate;

public abstract sealed class GameWrapper<T extends Game, M extends GameModule> permits ClientGameWrapper,ServerGameWrapper {

  private final T game;

  protected final Map<IRegistryDelegate<ModuleType>, M> modules;
  private final Collection<GameModule.Tickable> tickableModules;
  private final Map<IRegistryDelegate<ModuleType>, Synched> synched;

  public GameWrapper(T game) {
    this.game = game;

    var moduleBuilder = new ModuleBuilder<M>();

    this.buildModules(game, moduleBuilder);

    this.modules = moduleBuilder.modules().buildOrThrow();
    this.tickableModules = moduleBuilder.tickables().build();
    this.synched = moduleBuilder.synched().buildOrThrow();

    game.load();
  }

  protected abstract void buildModules(T game, ModuleBuilder<M> builder);

  public T getGame() {
    return this.game;
  }

  @Nullable
  public M getModule(ModuleType moduleType) {
    return this.modules.get(moduleType.delegate);
  }

  public void load() {
    this.modules.values().forEach(GameModule::load);
    this.game.started();
  }

  public void unload() {
    this.game.ended();
    this.modules.values().forEach(GameModule::unload);
  }

  public void tick() {
    this.tickableModules.forEach(GameModule.Tickable::tick);
    this.game.tick();
  }

  public Packet<?> buildSyncPacket(boolean writeAll) {
    var packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
    this.encode(packetBuffer, writeAll);
    return NetworkChannel.PLAY.getSimpleChannel()
        .toVanillaPacket(new SyncGameMessage(packetBuffer), NetworkDirection.PLAY_TO_CLIENT);
  }

  private void encode(FriendlyByteBuf out, boolean writeAll) {
    this.game.encode(out, writeAll);

    out.writeVarInt(this.modules.size());
    for (var entry : this.synched.entrySet()) {
      out.writeRegistryId(entry.getKey().get());
      entry.getValue().encode(out, writeAll);
    }
  }

  public void decode(FriendlyByteBuf in) {
    this.game.decode(in);

    int size = in.readVarInt();
    for (int i = 0; i < size; i++) {
      var moduleType = in.readRegistryIdSafe(ModuleType.class);
      var module = this.synched.get(moduleType.delegate);
      if (module == null) {
        throw new IllegalStateException(
            "Module not present with ID: " + moduleType.getRegistryName().toString());
      }
      module.decode(in);
    }
  }

  protected boolean requiresSync() {
    return this.game.requiresSync()
        || this.synched.values().stream().anyMatch(Synched::requiresSync);
  }

  final record ModuleBuilder<M extends GameModule> (
      ImmutableMap.Builder<IRegistryDelegate<ModuleType>, M> modules,
      ImmutableList.Builder<GameModule.Tickable> tickables,
      ImmutableMap.Builder<IRegistryDelegate<ModuleType>, Synched> synched) {

    ModuleBuilder() {
      this(ImmutableMap.builder(), ImmutableList.builder(), ImmutableMap.builder());
    }

    void registerModule(M module) {
      this.modules.put(module.getType().delegate, module);
      if (module instanceof GameModule.Tickable tickable) {
        this.tickables.add(tickable);
      }
      if (module instanceof Synched synched) {
        this.synched.put(module.getType().delegate, synched);
      }
    }
  }
}
