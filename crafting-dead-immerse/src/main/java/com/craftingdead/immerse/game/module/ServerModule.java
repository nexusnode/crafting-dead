package com.craftingdead.immerse.game.module;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.entity.player.ServerPlayerEntity;

public interface ServerModule extends Module {

  default void addPlayer(PlayerExtension<ServerPlayerEntity> player) {}

  default void removePlayer(PlayerExtension<ServerPlayerEntity> player) {}
}
