package com.craftingdead.mod.common.event;

import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class HandshakeEvent extends Event {

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
