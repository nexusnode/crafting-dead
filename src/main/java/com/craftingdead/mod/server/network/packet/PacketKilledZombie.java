package com.craftingdead.mod.server.network.packet;

import java.util.UUID;

import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;

public class PacketKilledZombie implements IPacket {

	private UUID playerUUID;

	public PacketKilledZombie() {
		;
	}

	public PacketKilledZombie(UUID playerUUID) {
		this.playerUUID = playerUUID;
	}

	@Override
	public void toBytes(ByteBuf out) throws Exception {
		ByteBufUtils.writeUUID(out, playerUUID);
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		playerUUID = ByteBufUtils.readUUID(in);
	}

}