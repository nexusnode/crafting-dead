package com.craftingdead.mod.masterserver.message.handshake;

import com.craftingdead.mod.masterserver.ConnectionState;

import io.netty.buffer.ByteBuf;
import sm0keysa1m0n.network.message.Message;
import sm0keysa1m0n.network.util.ByteBufUtil;

public class MessageHandshake implements Message {

	private int requestedState;

	public MessageHandshake() {
		;
	}

	public MessageHandshake(ConnectionState requestedState) {
		this.requestedState = requestedState.getId();
	}

	@Override
	public void toBytes(ByteBuf out) throws Exception {
		ByteBufUtil.writeVarInt(out, this.requestedState);
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		this.requestedState = ByteBufUtil.readVarInt(in);
	}

}
