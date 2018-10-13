package com.craftingdead.mod.common.multiplayer.network;

import java.lang.reflect.Method;
import java.util.EnumMap;

import com.craftingdead.mod.common.Proxy;
import com.craftingdead.mod.common.multiplayer.network.message.MessageHandler;
import com.craftingdead.mod.common.multiplayer.network.pipeline.NettyMessageChannelHandler;
import com.craftingdead.mod.common.multiplayer.network.pipeline.NettyMessageCodec;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import sm0keysa1m0n.network.message.Message;

/**
 * Used to wrap around all networking classes such as the {@link NettyMessageCodec},
 * making life simple when it comes to networking. Register and send your
 * messages here
 * 
 * @author Sm0keySa1m0n
 *
 */
public class NetworkWrapper {

	private EnumMap<Side, FMLEmbeddedChannel> channels;
	private NettyMessageCodec messageCodec;
	private Proxy mod;

	private static Class<?> defaultChannelPipeline;
	private static Method generateName;

	public NetworkWrapper(String channelName, Proxy mod) {
		messageCodec = new NettyMessageCodec();
		channels = NetworkRegistry.INSTANCE.newChannel(channelName, messageCodec);
		this.mod = mod;
	}

	private String generateName(ChannelPipeline pipeline, ChannelHandler handler) {
		try {
			return (String) generateName.invoke(defaultChannelPipeline.cast(pipeline), handler);
		} catch (Exception e) {
			throw new RuntimeException("It appears we somehow have a not-standard pipeline. Huh", e);
		}
	}

	/**
	 * Register a message and it's associated handler. The message will have the
	 * supplied discriminator byte. The message handler will be registered on the
	 * supplied side (this is the side where you want the message to be processed and
	 * acted upon).
	 *
	 * @param messageHandler     the message handler type
	 * @param requestMessageType the message type
	 * @param discriminator      a discriminator byte
	 * @param side               the side for the handler
	 */
	public <REQ extends Message, REPLY extends Message> void registerMessage(
			Class<? extends MessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType,
			int discriminator, Side side) {
		registerMessage(instantiate(messageHandler), requestMessageType, discriminator, side);
	}

	static <REQ extends Message, REPLY extends Message> MessageHandler<? super REQ, ? extends REPLY> instantiate(
			Class<? extends MessageHandler<? super REQ, ? extends REPLY>> handler) {
		try {
			return handler.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Register a message and it's associated handler. The message will have the
	 * supplied discriminator byte. The message handler will be registered on the
	 * supplied side (this is the side where you want the message to be processed and
	 * acted upon).
	 *
	 * @param messageHandler     the message handler instance
	 * @param requestMessageType the message type
	 * @param discriminator      a discriminator byte
	 * @param side               the side for the handler
	 */
	public <REQ extends Message, REPLY extends Message> void registerMessage(
			MessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> requestMessageType,
			int discriminator, Side side) {
		messageCodec.addDiscriminator(discriminator, requestMessageType);
		FMLEmbeddedChannel channel = channels.get(side);
		String type = channel.findChannelHandlerNameForType(NettyMessageCodec.class);
		if (side == Side.SERVER) {
			addServerHandlerAfter(channel, type, messageHandler, requestMessageType);
		} else {
			addClientHandlerAfter(channel, type, messageHandler, requestMessageType);
		}
	}

	private <REQ extends Message, REPLY extends Message, NH extends INetHandler> void addServerHandlerAfter(
			FMLEmbeddedChannel channel, String type, MessageHandler<? super REQ, ? extends REPLY> messageHandler,
			Class<REQ> requestType) {
		NettyMessageChannelHandler<REQ, REPLY> handler = getHandlerWrapper(messageHandler, Side.SERVER, requestType);
		channel.pipeline().addAfter(type, generateName(channel.pipeline(), handler), handler);
	}

	private <REQ extends Message, REPLY extends Message, NH extends INetHandler> void addClientHandlerAfter(
			FMLEmbeddedChannel channel, String type, MessageHandler<? super REQ, ? extends REPLY> messageHandler,
			Class<REQ> requestType) {
		NettyMessageChannelHandler<REQ, REPLY> handler = getHandlerWrapper(messageHandler, Side.CLIENT, requestType);
		channel.pipeline().addAfter(type, generateName(channel.pipeline(), handler), handler);
	}

	private <REPLY extends Message, REQ extends Message> NettyMessageChannelHandler<REQ, REPLY> getHandlerWrapper(
			MessageHandler<? super REQ, ? extends REPLY> messageHandler, Side side, Class<REQ> requestType) {
		return new NettyMessageChannelHandler<REQ, REPLY>(messageHandler, side, requestType, mod);
	}

	/**
	 * Construct a minecraft packet from the supplied message. Can be used where
	 * minecraft packets are required, such as
	 * {@link TileEntity#getDescriptionPacket()}.
	 *
	 * @param message The message to translate into minecraft packet form
	 * @return A minecraft {@link Packet} suitable for use in minecraft APIs
	 */
	public Packet<?> getPacketFrom(Message message) {
		return channels.get(Side.SERVER).generatePacketFrom(message);
	}

	/**
	 * Send this message to everyone. The {@link MessageHandler} for this message type
	 * should be on the CLIENT side.
	 *
	 * @param message The message to send
	 */
	public void sendToAll(Message message) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to the specified player. The {@link MessageHandler} for this
	 * message type should be on the CLIENT side.
	 *
	 * @param message The message to send
	 * @param player The player to send it to
	 */
	public void sendTo(Message message, EntityPlayerMP player) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to everyone within a certain range of a point. The
	 * {@link MessageHandler} for this message type should be on the CLIENT side.
	 *
	 * @param message The message to send
	 * @param point  The {@link TargetPoint} around which to send
	 */
	public void sendToAllAround(Message message, NetworkRegistry.TargetPoint point) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Sends this message to everyone tracking a point. The {@link MessageHandler}
	 * for this message type should be on the CLIENT side. The {@code range} field of
	 * the {@link TargetPoint} is ignored.
	 *
	 * @param message The message to send
	 * @param point  The tracked {@link TargetPoint} around which to send
	 */
	public void sendToAllTracking(Message message, NetworkRegistry.TargetPoint point) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.TRACKING_POINT);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Sends this message to everyone tracking an entity. The {@link MessageHandler}
	 * for this message type should be on the CLIENT side. This is not equivalent to
	 * {@link #sendToAllTracking(IMessage, TargetPoint)} because entities have
	 * different tracking distances based on their type.
	 *
	 * @param message The message to send
	 * @param entity The tracked entity around which to send
	 */
	public void sendToAllTracking(Message message, Entity entity) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.TRACKING_ENTITY);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(entity);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to everyone within the supplied dimension. The
	 * {@link MessageHandler} for this message type should be on the CLIENT side.
	 *
	 * @param message      The message to send
	 * @param dimensionId The dimension id to target
	 */
	public void sendToDimension(Message message, int dimensionId) {
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		channels.get(Side.SERVER).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	/**
	 * Send this message to the server. The {@link MessageHandler} for this message
	 * type should be on the SERVER side.
	 *
	 * @param message The message to send
	 */
	public void sendToServer(Message message) {
		channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
				.set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	static {
		try {
			defaultChannelPipeline = Class.forName("io.netty.channel.DefaultChannelPipeline");
			generateName = defaultChannelPipeline.getDeclaredMethod("generateName", ChannelHandler.class);
			generateName.setAccessible(true);
		} catch (Exception e) {
			// How is this possible?
			throw new RuntimeException("What? Netty isn't installed, what magic is this?", e);
		}
	}

}
