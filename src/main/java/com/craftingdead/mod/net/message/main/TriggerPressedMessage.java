package com.craftingdead.mod.net.message.main;

import lombok.Data;

@Data
public class TriggerPressedMessage {

  private final int entityId;
  private final boolean triggerPressed;
}
