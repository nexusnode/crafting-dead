package com.craftingdead.mod.common.network;

import java.lang.reflect.Method;
import java.util.EnumMap;

import com.craftingdead.mod.common.core.ISidedMod;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;

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
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkWrapper {

    private EnumMap<Side, FMLEmbeddedChannel> channels;
    private PacketCodec packetCodec;
    private ISidedMod<?> mod;

    private static Class<?> defaultChannelPipeline;
    private static Method generateName;

    public NetworkWrapper(String channelName, ISidedMod<?> mod) {
        packetCodec = new PacketCodec();
        channels = NetworkRegistry.INSTANCE.newChannel(channelName, packetCodec);
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
     * Register a packet and it's associated handler. The packet will have the
     * supplied discriminator byte. The packet handler will be registered on the
     * supplied side (this is the side where you want the packet to be processed and
     * acted upon).
     *
     * @param packetHandler     the packet handler type
     * @param requestPacketType the packet type
     * @param discriminator     a discriminator byte
     * @param side              the side for the handler
     */
    public <REQ extends IPacket, REPLY extends IPacket> void registerPacket(
            Class<? extends IPacketHandler<REQ, REPLY, CraftingDeadContext>> packetHandler,
            Class<REQ> requestPacketType, int discriminator, Side side) {
        registerPacket(instantiate(packetHandler), requestPacketType, discriminator, side);
    }

    static <REQ extends IPacket, REPLY extends IPacket> IPacketHandler<? super REQ, ? extends REPLY, CraftingDeadContext> instantiate(
            Class<? extends IPacketHandler<? super REQ, ? extends REPLY, CraftingDeadContext>> handler) {
        try {
            return handler.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Register a packet and it's associated handler. The packet will have the
     * supplied discriminator byte. The packet handler will be registered on the
     * supplied side (this is the side where you want the packet to be processed and
     * acted upon).
     *
     * @param packetHandler     the packet handler instance
     * @param requestPacketType the packet type
     * @param discriminator     a discriminator byte
     * @param side              the side for the handler
     */
    public <REQ extends IPacket, REPLY extends IPacket> void registerPacket(
            IPacketHandler<? super REQ, ? extends REPLY, CraftingDeadContext> packetHandler,
            Class<REQ> requestPacketType, int discriminator, Side side) {
        packetCodec.addDiscriminator(discriminator, requestPacketType);
        FMLEmbeddedChannel channel = channels.get(side);
        String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
        if (side == Side.SERVER) {
            addServerHandlerAfter(channel, type, packetHandler, requestPacketType);
        } else {
            addClientHandlerAfter(channel, type, packetHandler, requestPacketType);
        }
    }

    private <REQ extends IPacket, REPLY extends IPacket, NH extends INetHandler> void addServerHandlerAfter(
            FMLEmbeddedChannel channel, String type,
            IPacketHandler<? super REQ, ? extends REPLY, CraftingDeadContext> packetHandler, Class<REQ> requestType) {
        PacketChannelHandler<REQ, REPLY> handler = getHandlerWrapper(packetHandler, Side.SERVER, requestType);
        channel.pipeline().addAfter(type, generateName(channel.pipeline(), handler), handler);
    }

    private <REQ extends IPacket, REPLY extends IPacket, NH extends INetHandler> void addClientHandlerAfter(
            FMLEmbeddedChannel channel, String type,
            IPacketHandler<? super REQ, ? extends REPLY, CraftingDeadContext> packetHandler, Class<REQ> requestType) {
        PacketChannelHandler<REQ, REPLY> handler = getHandlerWrapper(packetHandler, Side.CLIENT, requestType);
        channel.pipeline().addAfter(type, generateName(channel.pipeline(), handler), handler);
    }

    private <REPLY extends IPacket, REQ extends IPacket> PacketChannelHandler<REQ, REPLY> getHandlerWrapper(
            IPacketHandler<? super REQ, ? extends REPLY, CraftingDeadContext> packetHandler, Side side,
            Class<REQ> requestType) {
        return new PacketChannelHandler<REQ, REPLY>(packetHandler, side, requestType, mod);
    }

    /**
     * Construct a minecraft packet from the supplied message. Can be used where
     * minecraft packets are required, such as
     * {@link TileEntity#getDescriptionPacket()}.
     *
     * @param packet The packet to translate into minecraft packet form
     * @return A minecraft {@link Packet} suitable for use in minecraft APIs
     */
    public Packet<?> getPacketFrom(IPacket packet) {
        return channels.get(Side.SERVER).generatePacketFrom(packet);
    }

    /**
     * Send this packet to everyone. The {@link IPacketHandler} for this packet type
     * should be on the CLIENT side.
     *
     * @param packet The packet to send
     */
    public void sendToAll(IPacket packet) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this packet to the specified player. The {@link IPacketHandler} for this
     * packet type should be on the CLIENT side.
     *
     * @param packet The packet to send
     * @param player The player to send it to
     */
    public void sendTo(IPacket packet, EntityPlayerMP player) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this packet to everyone within a certain range of a point. The
     * {@link IPacketHandler} for this packet type should be on the CLIENT side.
     *
     * @param packet The packet to send
     * @param point  The {@link TargetPoint} around which to send
     */
    public void sendToAllAround(IPacket packet, NetworkRegistry.TargetPoint point) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Sends this packet to everyone tracking a point. The {@link IPacketHandler}
     * for this packet type should be on the CLIENT side. The {@code range} field of
     * the {@link TargetPoint} is ignored.
     *
     * @param packet The packet to send
     * @param point  The tracked {@link TargetPoint} around which to send
     */
    public void sendToAllTracking(IPacket packet, NetworkRegistry.TargetPoint point) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.TRACKING_POINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Sends this packet to everyone tracking an entity. The {@link IPacketHandler}
     * for this packet type should be on the CLIENT side. This is not equivalent to
     * {@link #sendToAllTracking(IMessage, TargetPoint)} because entities have
     * different tracking distances based on their type.
     *
     * @param packet The packet to send
     * @param entity The tracked entity around which to send
     */
    public void sendToAllTracking(IPacket packet, Entity entity) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.TRACKING_ENTITY);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(entity);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this packet to everyone within the supplied dimension. The
     * {@link IPacketHandler} for this packet type should be on the CLIENT side.
     *
     * @param packet      The packet to send
     * @param dimensionId The dimension id to target
     */
    public void sendToDimension(IPacket packet, int dimensionId) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channels.get(Side.SERVER).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    /**
     * Send this packet to the server. The {@link IPacketHandler} for this message
     * type should be on the SERVER side.
     *
     * @param packet The packet to send
     */
    public void sendToServer(IPacket packet) {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET)
                .set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
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
