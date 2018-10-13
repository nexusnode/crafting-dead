package com.craftingdead.mod.common.multiplayer.network.pipeline;

import com.craftingdead.mod.common.Proxy;
import com.craftingdead.mod.common.multiplayer.network.message.MessageContext;
import com.craftingdead.mod.common.multiplayer.network.message.MessageHandler;
import com.google.common.base.Preconditions;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import sm0keysa1m0n.network.message.Message;

/**
 * Wraps around a {@link IPacketHandler}, handling any packets of the specified
 * type by using the {@link SimpleChannelInboundHandler}
 * 
 * @author Sm0keySa1m0n
 *
 * @param <REQ> - the packet to listen for
 * @param <REPLY> - the reply packet
 */
public class NettyMessageChannelHandler<REQ extends Message, REPLY extends Message> extends SimpleChannelInboundHandler<REQ> {

	private final MessageHandler<? super REQ, ? extends REPLY> messageHandler;
	private final Side side;
	private final Proxy mod;

	public NettyMessageChannelHandler(MessageHandler<? super REQ, ? extends REPLY> handler, Side side, Class<REQ> requestType,
			Proxy mod) {
		super(requestType);
		messageHandler = Preconditions.checkNotNull(handler, "Packet handler must not be null");
		this.side = side;
		this.mod = mod;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, REQ msg) throws Exception {
		INetHandler iNetHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
		MessageContext context = new MessageContext(iNetHandler, side, mod);
		REPLY result = messageHandler.processMessage(msg, context);
		if (result != null) {
			ctx.channel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.REPLY);
			ctx.writeAndFlush(result).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		}
	}

}