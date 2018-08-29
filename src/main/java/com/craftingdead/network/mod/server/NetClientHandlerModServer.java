package com.craftingdead.network.mod.server;

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
		;
	}

}
