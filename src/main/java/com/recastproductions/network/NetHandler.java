package com.recastproductions.network;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.recastproductions.network.packet.IHandshakePacket;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;

import io.netty.util.AttributeKey;

public abstract class NetHandler<HS extends IHandshakePacket> {

	public static final AttributeKey<NetHandler<?>> NET_HANDLER_ATTR = AttributeKey.valueOf("net_handler");
	
	private BiMap<Integer, Class<? extends IPacket>> packetMap = HashBiMap.create();
	
	private Map<Class<? extends IPacket>, IPacketHandler<?, ?, ?>> handlerMap = new HashMap<Class<? extends IPacket>, IPacketHandler<?, ?, ?>>();
	
	public NetHandler() {
		this.registerPackets();
	}

	/**
	 * Get the name of this {@link INetHandler}
	 * 
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * Get the handshake packet assigned to this {@link INetHandler}
	 * 
	 * @return the packet {@link Class}
	 */
	public abstract Class<HS> getHandshakePacket();
	
	/**
	 * Called to register packets
	 */
	protected abstract void registerPackets();

	public void registerPacket(int discriminator, Class<? extends IPacket> packet) {
		if (packetMap.containsKey(discriminator)) {
			throw new RuntimeException("Packet with discriminator " + discriminator + " already exists");
		} else {
			packetMap.put(discriminator, packet);
		}
	}
	
	public <P extends IPacket> void registerPacket(int discriminator, Class<P> packet, IPacketHandler<P, ?, ?> packetHandler) {
		this.registerPacket(discriminator, packet);
		this.handlerMap.put(packet, packetHandler);
	}

	public Class<? extends IPacket> getPacket(int discriminator) {
		return packetMap.get(discriminator);
	}

	public Integer getDiscriminitor(Class<? extends IPacket> packet) {
		return packetMap.inverse().get(packet);
	}
	
	@SuppressWarnings("unchecked")
	public <P extends IPacket> IPacketHandler<P, ?, ?> getPacketHandler(Class<P> packet) {
		return (IPacketHandler<P, ?, ?>) handlerMap.get(packet);
	}

}
