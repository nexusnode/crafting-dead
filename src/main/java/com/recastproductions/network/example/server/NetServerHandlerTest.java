package com.recastproductions.network.example.server;

import com.recastproductions.network.example.HandshakeTest;
import com.recastproductions.network.example.PacketTest;
import com.recastproductions.network.server.NetServerHandler;

import io.netty.channel.Channel;

public class NetServerHandlerTest extends NetServerHandler<HandshakeTest, SessionTest> {

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public Class<HandshakeTest> getHandshakePacketClass() {
		return HandshakeTest.class;
	}

	@Override
	public SessionTest processHandshake(HandshakeTest message, Channel ch) {
		return new SessionTest(ch, this);
	}

	@Override
	protected void registerPackets() {
		this.registerPacket(0, PacketTest.class, new PacketTest.PacketTestHandler());
	}

}
