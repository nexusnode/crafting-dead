package com.recastproductions.network.impl.client;

import com.recastproductions.network.impl.NetHandler;
import com.recastproductions.network.impl.Session;
import com.recastproductions.network.packet.IHandshakePacket;

import io.netty.channel.Channel;

public abstract class NetClientHandler<HS extends IHandshakePacket, S extends Session<?>> extends NetHandler<HS> {
	
	protected abstract S newSession(Channel ch);
	
	protected abstract HS getHandshakePacket();
		
}
