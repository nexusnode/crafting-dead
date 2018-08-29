package com.recastproductions.network.impl.pipeline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.recastproductions.network.impl.NetHandler;
import com.recastproductions.network.impl.NetworkRegistry;
import com.recastproductions.network.impl.PacketHeartbeat;
import com.recastproductions.network.impl.client.NetworkRegistryClient;
import com.recastproductions.network.packet.IHandshakePacket;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<IPacket> {

	public static final String PIPELINE_NAME = "packet_endcoder";

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	protected void encode(ChannelHandlerContext ctx, IPacket msg, ByteBuf out) throws Exception {
		if (msg instanceof IHandshakePacket) {
			NetworkRegistry registry = ctx.channel().attr(NetworkRegistry.INSTANCE_ATTR).get();
			if (registry instanceof NetworkRegistryClient) {
				String netHandlerName = ((NetworkRegistryClient) registry).getNetHandler().getName();
				// Write discriminator
				ByteBufUtils.writeVarInt(out, -1);
				// Write net handler name
				ByteBufUtils.writeUTF8(out, netHandlerName);
				// Write message
				try {
					msg.toBytes(out);
				} catch (Exception e) {
					LOGGER.warn("An exception was thrown while encoding " + msg.getClass().getCanonicalName(), e);
				}
			} else {
				LOGGER.warn("Sending handshake packet from wrong side!");
				return;
			}
		} else if (msg instanceof PacketHeartbeat) {
			ByteBufUtils.writeVarInt(out, -2);
		} else {
			NetHandler<?> handler = ctx.channel().attr(NetHandler.NET_HANDLER_ATTR).get();
			if (handler != null) {
				Integer discriminator = handler.getDiscriminator(msg.getClass());
				if (discriminator != null) {
					// Write discriminator
					ByteBufUtils.writeVarInt(out, discriminator);
					// Write message
					try {
						msg.toBytes(out);
					} catch (Exception e) {
						LOGGER.error("An exception was thrown while encoding a packet", e);
					}
				} else {
					LOGGER.warn("No packet discriminator found for {}", msg.getClass().getCanonicalName());
				}
			} else {
				LOGGER.warn("No NetHandler found!");
				return;
			}
		}
	}

}
