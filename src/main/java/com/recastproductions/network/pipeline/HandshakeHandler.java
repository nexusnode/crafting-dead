package com.recastproductions.network.pipeline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.recastproductions.network.NetHandler;
import com.recastproductions.network.Session;
import com.recastproductions.network.packet.IHandshakePacket;
import com.recastproductions.network.server.NetServerHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HandshakeHandler<REQ extends IHandshakePacket, H extends NetServerHandler<REQ, ?>>
		extends SimpleChannelInboundHandler<REQ> {

	private static final Logger LOGGER = LogManager.getLogger();

	private final H netHandler;

	public HandshakeHandler(Class<REQ> handshakePacketType, H netHandler) {
		super(handshakePacketType);
		this.netHandler = netHandler;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, REQ msg) throws Exception {
		Session<?> session = netHandler.processHandshake(msg, ctx.channel());
		if (session != null) {
			ctx.channel().attr(NetHandler.NET_HANDLER_ATTR).set(netHandler);
			ctx.channel().pipeline().addLast(Session.PIPELINE_NAME, session);
			LOGGER.info("A new session has been established with the address {}", ctx.channel().remoteAddress().toString());
		} else {
			LOGGER.info("{} has failed to establish a session. Failed the handshake?");
		}
	}

}
