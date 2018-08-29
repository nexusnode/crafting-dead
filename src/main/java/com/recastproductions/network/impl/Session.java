package com.recastproductions.network.impl;

import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketContext;
import com.recastproductions.network.packet.IPacketHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public abstract class Session<NH extends NetHandler<?>> extends SimpleChannelInboundHandler<IPacket>
		implements IPacketContext {

	public static final String PIPELINE_NAME = "session";

	private final NH netHandler;

	private final Channel channel;

	public Session(Channel ch, NH handler) {
		this.channel = ch;
		this.netHandler = handler;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket packet) throws Exception {
		this.handlePacket(packet);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			ctx.writeAndFlush(new PacketHeartbeat()).addListener((ChannelFutureListener) future -> {
				if (!future.isSuccess()) {
					future.channel().close();
				}
			});
		}
	}

	protected <P extends IPacket> void handlePacket(P packet) {
		@SuppressWarnings("unchecked")
		IPacketHandler<P, ?, ? super Session<?>> handler = (IPacketHandler<P, ?, ? super Session<?>>) netHandler
				.getPacketHandler(packet.getClass());
		if (handler != null) {
			handler.processPacket(packet, this);
		}
	}

	public NH getNetHandler() {
		return this.netHandler;
	}

	public Channel getChannel() {
		return this.channel;
	}

}
