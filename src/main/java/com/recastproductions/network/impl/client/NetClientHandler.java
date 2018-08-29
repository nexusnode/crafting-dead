package com.recastproductions.network.impl.client;

import javax.annotation.Nullable;

import com.recastproductions.network.impl.NetHandler;
import com.recastproductions.network.impl.Session;
import com.recastproductions.network.packet.IHandshakePacket;

import io.netty.channel.Channel;

public abstract class NetClientHandler<HS extends IHandshakePacket, S extends Session<?>> extends NetHandler<HS> {

	private S currentSession;

	protected S onConnected(Channel ch) {
		this.currentSession = this.newSession(ch);
		return this.currentSession;
	}

	protected abstract S newSession(Channel ch);

	protected abstract HS getHandshakePacket();

	@Nullable
	public S getCurrentSession() {
		return this.currentSession;
	}

}
