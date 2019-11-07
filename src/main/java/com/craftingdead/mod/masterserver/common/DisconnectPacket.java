package com.craftingdead.mod.masterserver.common;

import com.craftingdead.network.protocol.packet.IPacket;
import io.netty.buffer.ByteBuf;

public class DisconnectPacket implements IPacket {

  @Override
  public void encode(ByteBuf out) {}

  @Override
  public void decode(ByteBuf in) {}
}
