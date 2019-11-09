package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.net.NetworkChannel;
import com.craftingdead.mod.net.message.main.TriggerPressedMessage;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class ClientPlayer extends DefaultPlayer<ClientPlayerEntity> {

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
  public void setTriggerPressed(boolean triggerPressed) {
    super.setTriggerPressed(triggerPressed);
    NetworkChannel.MAIN.getSimpleChannel()
        .sendToServer(new TriggerPressedMessage(0, triggerPressed));
  }
}
