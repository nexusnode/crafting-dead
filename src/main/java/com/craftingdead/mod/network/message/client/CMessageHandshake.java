package com.craftingdead.mod.network.message.client;

import java.io.IOException;

import com.craftingdead.mod.core.CraftingDead;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CMessageHandshake implements IMessage {

	private String[] mods;
	
	public CMessageHandshake() {
		;
	}
	
	public CMessageHandshake(String[] mods) {
		this.mods = mods;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			mods = ByteBufUtils.readStringArray(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			ByteBufUtils.writeStringArray(buf, mods);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getMods() {
		return this.mods;
	}

	public static class CMessageHandlerHandshake implements IMessageHandler<CMessageHandshake, IMessage> {

		@Override
		public IMessage onMessage(CMessageHandshake message, MessageContext ctx) {
			CraftingDead.instance().getLogicalServer().onHandshake(ctx.getServerHandler().player, message);
			return null;
		}

	}

}
