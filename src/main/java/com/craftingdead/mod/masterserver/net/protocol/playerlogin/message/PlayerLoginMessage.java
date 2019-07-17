package com.craftingdead.mod.masterserver.net.protocol.playerlogin.message;

import java.util.UUID;
import com.craftingdead.network.protocol.IMessage;
import lombok.Data;

@Data
public class PlayerLoginMessage implements IMessage {

  private final UUID id;
  private final String username;
  private final String clientToken;
  private final String accessToken;
  private final String version;
}
