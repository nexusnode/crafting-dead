package com.craftingdead.mod.masterserver.net.protocol.serverlogin.message;

import com.craftingdead.network.protocol.IMessage;
import lombok.Data;

@Data
public class ServerLoginMessage implements IMessage {

  private final String minecraftHost;
  private final int minecraftPort;
}
