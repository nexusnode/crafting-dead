package com.craftingdead.core.capability.living.player;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Util;

public class SelfPlayer extends DefaultPlayer<ClientPlayerEntity> {

  private static final int DOUBLE_CLICK_DURATION = 500;

  private boolean wasSneaking;
  private long lastSneakPressTime;

  public SelfPlayer(ClientPlayerEntity entity) {
    super(entity);
  }

  public void updateMetadata(int daysSurvived, int zombiesKilled, int playersKilled, int water,
      int maxWater, int stamina, int maxStamina) {
    this.daysSurvived = daysSurvived;
    this.zombiesKilled = zombiesKilled;
    this.playersKilled = playersKilled;
    this.water = water;
    this.maxWater = maxWater;
    this.stamina = stamina;
    this.maxStamina = maxStamina;
  }

  @Override
  public void setFreezeMovement(boolean freezeMovement) {
    super.setFreezeMovement(freezeMovement);
    ((ClientDist) CraftingDead.getInstance().getModDist()).setFreezeMovementInput(freezeMovement);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.getEntity().isSneaking() != this.wasSneaking) {
      if (this.getEntity().isSneaking()) {
        final long currentTime = Util.milliTime();
        if (currentTime - this.lastSneakPressTime <= DOUBLE_CLICK_DURATION) {
          this.setCrouching(true, true);
        }
        this.lastSneakPressTime = Util.milliTime();
      } else {
        this.setCrouching(false, true);
      }
      this.wasSneaking = this.getEntity().isSneaking();
    }
  }
}
