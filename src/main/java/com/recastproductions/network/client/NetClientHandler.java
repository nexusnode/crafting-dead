package com.recastproductions.network.client;

import com.recastproductions.network.NetHandler;
import com.recastproductions.network.Session;
import com.recastproductions.network.packet.IHandshakePacket;

import io.netty.channel.Channel;

public abstract class NetClientHandler<HS extends IHandshakePacket, S extends Session<?>> extends NetHandler<HS> {
	
	public abstract S newSession(Channel ch);
		
}
