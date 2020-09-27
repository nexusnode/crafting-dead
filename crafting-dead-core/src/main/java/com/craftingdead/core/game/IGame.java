package com.craftingdead.core.game;

import com.craftingdead.core.capability.living.Player;
import net.minecraft.entity.player.PlayerEntity;

public interface IGame<T extends ITeam, P extends Player<? extends E>, E extends PlayerEntity> {

  default P getPlayer(E playerEntity) {
    return Player.get(playerEntity);
  }

  P createPlayer(E playerEntity);

  T getTeam(P player);

  void setTeam(P player, T team);

  GameType getGameType();
}
