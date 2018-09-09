package com.craftingdead.mod.common.multiplayer.network.pipeline;

import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketContextMod;
import com.google.common.base.Preconditions;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.INetHandler;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Wraps around a {@link IPacketHandler}, handling any packets of the specified
 * type by using the {@link SimpleChannelInboundHandler}
 * 
 * @author Sm0keySa1m0n
 *
 * @param <REQ> - the packet to listen for
 * @param <REPLY> - the reply packet
 */
public class PacketChannelHandler<REQ extends IPacket, REPLY extends IPacket> extends SimpleChannelInboundHandler<REQ> {

	private final IPacketHandler<? super REQ, ? extends REPLY, PacketContextMod> packetHandler;
	private final Side side;
	private final ISidedMod<?, ?> mod;

	public PacketChannelHandler(IPacketHandler<? super REQ, ? extends REPLY, PacketContextMod> handler, Side side,
			Class<REQ> requestType, ISidedMod<?, ?> mod) {
		super(requestType);
		packetHandler = Preconditions.checkNotNull(handler, "Packet handler must not be null");
		this.side = side;
		this.mod = mod;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, REQ msg) throws Exception {
		INetHandler iNetHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
		PacketContextMod context = new PacketContextMod(iNetHandler, side, mod);
		REPLY result = packetHandler.processPacket(msg, context);
		if (result != null) {
			ctx.channel().attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.REPLY);
			ctx.writeAndFlush(result).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		}
	}

}