package com.craftingdead.mod.message.server;

import lombok.Data;

@Data
public class ServerTriggerPressedMessage {

	private final int entityId;
	private final boolean triggerPressed;
}
