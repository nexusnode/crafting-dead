package com.craftingdead.mod.masterserver.net.protocol.playerlogin.message;

import com.craftingdead.network.protocol.IMessage;
import lombok.Data;

@Data
public class PlayerLoginResultMessage implements IMessage {

  private final boolean success;
}
