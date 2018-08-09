package com.recastproductions.network.server;

import java.util.Map.Entry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.recastproductions.network.NetHandler;
import com.recastproductions.network.NetworkRegistry;
import com.recastproductions.network.packet.IHandshakePacket;
import com.recastproductions.network.pipeline.HandshakeHandler;

import io.netty.channel.Channel;

public class NetworkRegistryServer extends NetworkRegistry {

	private BiMap<String, Class<? extends IHandshakePacket>> handshakeMap = HashBiMap.create();
	private BiMap<String, NetServerHandler<?, ?>> handlerMap = HashBiMap.create();

	public void registerNetHandler(NetServerHandler<?, ?> handler) {
		this.handlerMap.put(handler.getName(), handler);
		this.handshakeMap.put(handler.getName(), handler.getHandshakePacket());
	}

	public String getName(Class<? extends IHandshakePacket> handshakeMessage) {
		return handshakeMap.inverse().get(handshakeMessage);
	}

	public NetHandler<?> getNetHandler(String name) {
		return handlerMap.get(name);
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		for (Entry<String, NetServerHandler<?, ?>> entry : handlerMap.entrySet()) {
			ch.pipeline().addLast(entry.getKey(), this.wrapHandshakeHandler((NetServerHandler<?, ?>) entry.getValue()));
		}
	}

	private <T extends IHandshakePacket, SH extends NetServerHandler<T, ?>> HandshakeHandler<T, SH> wrapHandshakeHandler(
			SH netHandler) {
		return new HandshakeHandler<T, SH>(netHandler.getHandshakePacket(), netHandler);
	}

}
