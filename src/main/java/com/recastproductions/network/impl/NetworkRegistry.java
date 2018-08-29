package com.recastproductions.network.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.recastproductions.network.impl.pipeline.PacketDecoder;
import com.recastproductions.network.impl.pipeline.PacketEncoder;
import com.recastproductions.network.pipline.Varint21FrameDecoder;
import com.recastproductions.network.pipline.Varint21FrameEncoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

@ChannelInboundHandlerAdapter.Sharable
public abstract class NetworkRegistry extends ChannelInboundHandlerAdapter {

	public static final Logger LOGGER = LogManager.getLogger();

	public static final AttributeKey<NetworkRegistry> INSTANCE_ATTR = AttributeKey.valueOf("network_registry");

	protected abstract void initChannel(Channel ch) throws Exception;

	public Initializer getChannelInitializer() {
		return new Initializer();
	}

	public class Initializer extends ChannelInitializer<Channel> {

		@Override
		protected void initChannel(Channel ch) throws Exception {
			ch.attr(INSTANCE_ATTR).set(NetworkRegistry.this);

			ch.pipeline().addLast(new Varint21FrameDecoder()).addLast(PacketDecoder.PIPELINE_NAME, new PacketDecoder());

			ch.pipeline().addLast(new Varint21FrameEncoder()).addLast(PacketEncoder.PIPELINE_NAME, new PacketEncoder())
					.addLast(NetworkRegistry.this);

			ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(1, 0, 0));

			NetworkRegistry.this.initChannel(ch);
		}

	}

}
