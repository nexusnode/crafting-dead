package com.craftingdead.mod.net.message.main;

public class TriggerPressedMessage {

  private final int entityId;
  private final boolean triggerPressed;

  public TriggerPressedMessage(int entityId, boolean triggerPressed) {
    this.entityId = entityId;
    this.triggerPressed = triggerPressed;
  }

  public int getEntityId() {
    return this.entityId;
  }

  public boolean isTriggerPressed() {
    return this.triggerPressed;
  }
}
