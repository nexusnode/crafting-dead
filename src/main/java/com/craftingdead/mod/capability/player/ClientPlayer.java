package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.net.NetworkChannel;
import com.craftingdead.mod.net.message.main.TriggerPressedMessage;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class ClientPlayer extends DefaultPlayer<ClientPlayerEntity> {

  public ClientPlayer(ClientPlayerEntity entity) {
    super(entity);
  }

  public void updateStatistics(int daysSurvived, int zombiesKilled, int playersKilled) {
    this.daysSurvived = daysSurvived;
    this.zombiesKilled = zombiesKilled;
    this.playersKilled = playersKilled;
  }

  @Override
  public void setTriggerPressed(boolean triggerPressed) {
    super.setTriggerPressed(triggerPressed);
    NetworkChannel.MAIN.getSimpleChannel()
        .sendToServer(new TriggerPressedMessage(0, triggerPressed));
  }
}
