package com.craftingdead.mod.masterserver.net.protocol.login.message;

import com.craftingdead.network.protocol.IMessage;
import lombok.Data;

@Data
public class LoginResultMessage implements IMessage {

  private final boolean success;
}
