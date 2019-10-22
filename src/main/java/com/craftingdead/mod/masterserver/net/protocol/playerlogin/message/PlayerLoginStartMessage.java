package com.craftingdead.mod.masterserver.net.protocol.playerlogin.message;

import com.craftingdead.network.protocol.IMessage;
import com.craftingdead.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PlayerLoginStartMessage implements IMessage {

  private final UUID id;
  private final String username;
  private final String version;

  public PlayerLoginStartMessage(ByteBuf in) throws IOException {
    this.id = ByteBufUtil.readId(in);
    this.username = ByteBufUtil.readUtf8(in);
    this.version = ByteBufUtil.readUtf8(in);
  }

  public void encode(ByteBuf out) throws IOException {
    ByteBufUtil.writeId(out, this.id);
    ByteBufUtil.writeUtf8(out, this.username);
    ByteBufUtil.writeUtf8(out, this.version);
  }
}
