package com.recastproductions.network.pipeline;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.recastproductions.network.NetHandler;
import com.recastproductions.network.NetworkRegistry;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.server.NetworkRegistryServer;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	public static final String PIPELINE_NAME = "packet_decoder";

	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() != 0) {
			int discriminator = ByteBufUtils.readVarInt(in);
			Class<? extends IPacket> packetClass = null;

			if (discriminator == -1) {
				NetworkRegistry registry = ctx.channel().attr(NetworkRegistry.INSTANCE_ATTR).get();
				if (registry instanceof NetworkRegistryServer) {
					String netHandlerName = ByteBufUtils.readUTF8(in);
					packetClass = ((NetworkRegistryServer) registry).getNetHandler(netHandlerName)
							.getHandshakePacketClass();
				}
			} else {
				NetHandler<?> handler = ctx.channel().attr(NetHandler.NET_HANDLER_ATTR).get();
				if (handler != null) {
					packetClass = handler.getPacket(discriminator);
				}
			}

			if (packetClass != null) {
				IPacket packet = packetClass.newInstance();
				packet.fromBytes(in);
				if (in.readableBytes() > 0) {
					throw new IOException("Packet " + discriminator + " (" + packetClass.getCanonicalName()
							+ ") was larger than I expected, found " + in.readableBytes()
							+ " bytes extra whilst reading packet " + discriminator);
				}
				out.add(packet);
			} else {
				LOGGER.info("Bad packet discriminator {}", discriminator);
			}
		}
	}

}
