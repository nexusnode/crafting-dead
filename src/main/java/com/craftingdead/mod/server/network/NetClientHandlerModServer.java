package com.craftingdead.mod.server.network;

import com.craftingdead.mod.server.network.packet.PacketKilledPlayer;
import com.craftingdead.mod.server.network.packet.PacketKilledZombie;
import com.recastproductions.network.impl.client.NetClientHandler;

import io.netty.channel.Channel;

public class NetClientHandlerModServer extends NetClientHandler<PacketHandshakeModServer, SessionModServer> {

	@Override
	protected SessionModServer newSession(Channel ch) {
		return new SessionModServer(ch, this);
	}

	@Override
	protected PacketHandshakeModServer getHandshakePacket() {
		return new PacketHandshakeModServer();
	}

	@Override
	public String getName() {
		return "mod_server";
	}

	@Override
	protected void registerPackets() {
		this.registerPacket(0x00, PacketKilledZombie.class);
		this.registerPacket(0x01, PacketKilledPlayer.class);
	}

}
