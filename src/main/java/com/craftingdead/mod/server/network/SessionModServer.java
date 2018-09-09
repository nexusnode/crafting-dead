package com.craftingdead.mod.server.network;

import com.recastproductions.network.impl.Session;

import io.netty.channel.Channel;

public class SessionModServer extends Session<NetClientHandlerModServer> {

	public SessionModServer(Channel ch, NetClientHandlerModServer handler) {
		super(ch, handler);
	}

}
