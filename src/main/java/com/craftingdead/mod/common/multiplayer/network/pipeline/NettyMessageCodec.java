package com.craftingdead.mod.common.multiplayer.network.pipeline;

import com.craftingdead.mod.common.CraftingDead;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import sm0keysa1m0n.network.message.Message;

/**
 * Used to register packets and encode/decode them
 * 
 * @author Sm0keySa1m0n
 *
 */
public class NettyMessageCodec extends FMLIndexedMessageToMessageCodec<Message> {

	@Override
	public void encodeInto(ChannelHandlerContext ctx, Message msg, ByteBuf target) throws Exception {
		try {
			msg.toBytes(target);
		} catch (Exception e) {
			CraftingDead.LOGGER.warn("An exception was thrown while encoding " + msg.getClass().getCanonicalName(), e);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, Message msg) {
		try {
			msg.fromBytes(source);
		} catch (Exception e) {
			CraftingDead.LOGGER.warn("An exception was thrown while decoding " + msg.getClass().getCanonicalName(), e);
		}
	}

}
