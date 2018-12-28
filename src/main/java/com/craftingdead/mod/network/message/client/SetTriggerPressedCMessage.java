package com.craftingdead.mod.network.message.client;

import com.craftingdead.mod.init.ModCapabilities;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetTriggerPressedCMessage implements IMessage {

	private boolean triggerPressed;

	public SetTriggerPressedCMessage() {
		;
	}

	public SetTriggerPressedCMessage(boolean triggerPressed) {
		this.triggerPressed = triggerPressed;
	}

	@Override
	public void toBytes(ByteBuf out) {
		out.writeBoolean(this.triggerPressed);
	}

	@Override
	public void fromBytes(ByteBuf in) {
		this.triggerPressed = in.readBoolean();
	}

	public static class SetTriggerPressedCHandler implements IMessageHandler<SetTriggerPressedCMessage, IMessage> {

		@Override
		public IMessage onMessage(SetTriggerPressedCMessage msg, MessageContext ctx) {
			ctx.getServerHandler().player.getCapability(ModCapabilities.PLAYER, null)
					.setTriggerPressed(msg.triggerPressed);
			return null;
		}

	}

}
