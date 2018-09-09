package com.craftingdead.mod.server.network.packet;

import java.util.UUID;

import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;

public class PacketKilledPlayer implements IPacket {

	private UUID playerUUID, killedUUID;

	public PacketKilledPlayer() {
		;
	}

	public PacketKilledPlayer(UUID playerUUID, UUID killedUUID) {
		this.playerUUID = playerUUID;
		this.killedUUID = killedUUID;
	}

	@Override
	public void toBytes(ByteBuf out) throws Exception {
		ByteBufUtils.writeUUID(out, playerUUID);
		ByteBufUtils.writeUUID(out, killedUUID);
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		playerUUID = ByteBufUtils.readUUID(in);
		killedUUID = ByteBufUtils.readUUID(in);
	}

}
