package com.craftingdead.mod.masterserver.message.server;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import sm0keysa1m0n.network.message.Message;
import sm0keysa1m0n.network.util.ByteBufUtil;

public class MessageKilledZombie implements Message {

	private UUID playerUUID;

	public MessageKilledZombie() {
		;
	}

	public MessageKilledZombie(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	@Override
	public void toBytes(ByteBuf out) throws Exception {
		ByteBufUtil.writeUUID(out, playerUUID);
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		playerUUID = ByteBufUtil.readUUID(in);
	}

}