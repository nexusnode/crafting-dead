package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.network.message.main.PlayerActionMessage;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Util;

public class ClientPlayer extends DefaultPlayer<ClientPlayerEntity> {

  private static final int DOUBLE_CLICK_DURATION = 500;

  private boolean wasHoldingSneakKey;
  private long lastSneakPressTime;
  private boolean crouching;

  public ClientPlayer(ClientPlayerEntity entity) {
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
  public void tick() {
    super.tick();

    if (this.entity.isHoldingSneakKey() != this.wasHoldingSneakKey) {
      if (this.entity.isHoldingSneakKey()) {
        final long currentTime = Util.milliTime();
        if (currentTime - this.lastSneakPressTime <= DOUBLE_CLICK_DURATION) {
          this.crouching = true;
        }
        this.lastSneakPressTime = Util.milliTime();
      } else {
        this.crouching = false;
      }
    }
    this.wasHoldingSneakKey = this.entity.isHoldingSneakKey();

    if (this.crouching) {
      this.entity.setPose(Pose.SWIMMING);
    }

    if (ClientDist.RELOAD.isKeyDown()) {
      this.reload(true);
    }
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed, boolean sendUpdate) {
    super.setTriggerPressed(triggerPressed, sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(
              new PlayerActionMessage(0, triggerPressed ? PlayerActionMessage.Action.TRIGGER_PRESSED
                  : PlayerActionMessage.Action.TRIGGER_RELEASED));
    }
  }

  @Override
  public void toggleAiming(boolean sendUpdate) {
    super.toggleAiming(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(new PlayerActionMessage(0, PlayerActionMessage.Action.TOGGLE_AIMING));
    }
  }

  @Override
  public void toggleFireMode(boolean sendUpdate) {
    super.toggleFireMode(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(new PlayerActionMessage(0, PlayerActionMessage.Action.TOGGLE_FIRE_MODE));
    }
  }

  @Override
  public void reload(boolean sendUpdate) {
    super.reload(sendUpdate);
    if (sendUpdate) {
      NetworkChannel.MAIN
          .getSimpleChannel()
          .sendToServer(new PlayerActionMessage(0, PlayerActionMessage.Action.RELOAD));
    }
  }

  @Override
  public void openPlayerContainer() {
    super.openPlayerContainer();
    NetworkChannel.MAIN
        .getSimpleChannel()
        .sendToServer(new PlayerActionMessage(0, PlayerActionMessage.Action.OPEN_PLAYER_CONTAINER));
  }
}
