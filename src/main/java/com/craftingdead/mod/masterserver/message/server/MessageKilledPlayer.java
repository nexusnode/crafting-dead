package com.craftingdead.mod.masterserver.message.server;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import sm0keysa1m0n.network.message.Message;
import sm0keysa1m0n.network.util.ByteBufUtil;

public class MessageKilledPlayer implements Message {

	private UUID playerUUID, killedUUID;

	public MessageKilledPlayer() {
		;
	}

	public MessageKilledPlayer(UUID playerUUID, UUID killedUUID) {
		this.playerUUID = playerUUID;
		this.killedUUID = killedUUID;
	}

	@Override
	public void toBytes(ByteBuf out) throws Exception {
		ByteBufUtil.writeUUID(out, playerUUID);
		ByteBufUtil.writeUUID(out, killedUUID);
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		playerUUID = ByteBufUtil.readUUID(in);
		killedUUID = ByteBufUtil.readUUID(in);
	}

}
