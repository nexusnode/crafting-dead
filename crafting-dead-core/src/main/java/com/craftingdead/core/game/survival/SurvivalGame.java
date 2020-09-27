package com.craftingdead.core.game.survival;

import com.craftingdead.core.game.AbstractGameServer;
import com.craftingdead.core.game.GameType;
import com.craftingdead.core.game.GameTypes;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.player.ServerPlayerEntity;

public class SurvivalGame
    extends AbstractGameServer<SurvivorsTeam, SurvivalPlayer<ServerPlayerEntity>> {

  public SurvivalGame(GameType gameType) {
    super(GameTypes.SURVIVAL, ImmutableSet.of(new SurvivorsTeam()));
  }

  @Override
  public SurvivalPlayer<ServerPlayerEntity> createPlayer(ServerPlayerEntity playerEntity) {
    return new SurvivalPlayer<>(playerEntity);
  }
}
