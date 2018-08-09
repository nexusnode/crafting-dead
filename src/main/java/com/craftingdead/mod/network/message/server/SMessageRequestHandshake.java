package com.craftingdead.mod.network.message.server;

import com.craftingdead.mod.core.CraftingDead;
import com.craftingdead.mod.network.message.client.CMessageHandshake;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SMessageRequestHandshake implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
		;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		;
	}

	public static class SMessageHandlerRequestHandshake
			implements IMessageHandler<SMessageRequestHandshake, CMessageHandshake> {

		@Override
		public CMessageHandshake onMessage(SMessageRequestHandshake message, MessageContext ctx) {
			return CraftingDead.instance().getClient().getHandshakePacket();
		}

	}

}
