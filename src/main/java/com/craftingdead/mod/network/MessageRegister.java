package com.craftingdead.mod.network;

import com.craftingdead.mod.network.message.client.CMessageHandshake;
import com.craftingdead.mod.network.message.client.CMessageHandshake.CMessageHandlerHandshake;
import com.craftingdead.mod.network.message.server.SMessageRequestHandshake;
import com.craftingdead.mod.network.message.server.SMessageRequestHandshake.SMessageHandlerRequestHandshake;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MessageRegister {

	private static int messageId = -1;

	private static int getNextMessageId() {
		messageId += 1;
		return messageId;
	}

	public static void registerPackets(SimpleNetworkWrapper wrapper) {
		wrapper.registerMessage(CMessageHandlerHandshake.class, CMessageHandshake.class, getNextMessageId(),
				Side.SERVER);
		wrapper.registerMessage(SMessageHandlerRequestHandshake.class, SMessageRequestHandshake.class,
				getNextMessageId(), Side.CLIENT);
	}
}
