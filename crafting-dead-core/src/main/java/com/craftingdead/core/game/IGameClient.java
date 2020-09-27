package com.craftingdead.core.game;

import com.craftingdead.core.capability.living.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public interface IGameClient<T extends ITeam, P extends Player<? extends AbstractClientPlayerEntity>>
    extends IGame<T, P, AbstractClientPlayerEntity> {

  default void renderHud(Minecraft minecraft, ClientPlayerEntity playerEntity, int width,
      int height, float partialTicks) {
    this.renderHud(minecraft, this.getPlayer(playerEntity), width, height, partialTicks);
  }

  void renderHud(Minecraft minecraft, P player, int width, int height, float partialTicks);
}
