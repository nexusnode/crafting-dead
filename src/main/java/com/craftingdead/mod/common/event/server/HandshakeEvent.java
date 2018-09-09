package com.craftingdead.mod.common.event.server;

import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake;

import net.minecraft.entity.player.EntityPlayerMP;

public class HandshakeEvent {

	private final EntityPlayerMP player;
	private final PacketHandshake handshakePacket;

	public HandshakeEvent(EntityPlayerMP player, PacketHandshake handshakePacket) {
		this.player = player;
		this.handshakePacket = handshakePacket;
	}

	public EntityPlayerMP getPlayer() {
		return this.player;
	}

	public PacketHandshake getPacketHandshake() {
		return this.handshakePacket;
	}

}
