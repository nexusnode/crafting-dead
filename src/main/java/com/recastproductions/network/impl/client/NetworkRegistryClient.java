package com.recastproductions.network.impl.client;

import com.recastproductions.network.impl.NetHandler;
import com.recastproductions.network.impl.NetworkRegistry;
import com.recastproductions.network.impl.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class NetworkRegistryClient extends NetworkRegistry {

	private NetClientHandler<?, ?> netHandler;

	public NetworkRegistryClient(NetClientHandler<?, ?> handler) {
		this.netHandler = handler;
	}

	public NetClientHandler<?, ?> getNetHandler() {
		return this.netHandler;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		LOGGER.info("Sending handshake packet");
		try {
			ctx.channel().attr(NetHandler.NET_HANDLER_ATTR).set(netHandler);
			ctx.channel().writeAndFlush(this.netHandler.getHandshakePacket()).channel().pipeline()
					.addLast(Session.PIPELINE_NAME, this.netHandler.onConnected(ctx.channel()));
		} catch (Exception e) {
			throw new RuntimeException("Could not send handshake packet", e);
		}
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		;
	}

}
