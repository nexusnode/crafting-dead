package com.craftingdead.mod.common.multiplayer.message;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.multiplayer.PlayerMP;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateTriggerStatus implements IMessage {

	private boolean triggerDown;

	public MessageUpdateTriggerStatus() {
		;
	}

	public MessageUpdateTriggerStatus(boolean triggerDown) {
		this.triggerDown = triggerDown;
	}

	@Override
	public void toBytes(ByteBuf out) {
		out.writeBoolean(this.triggerDown);
	}

	@Override
	public void fromBytes(ByteBuf in) {
		this.triggerDown = in.readBoolean();
	}

	public static class MessageHandlerUpdateTriggerStatus
			implements IMessageHandler<MessageUpdateTriggerStatus, IMessage> {

		@Override
		public IMessage onMessage(MessageUpdateTriggerStatus msg, MessageContext ctx) {
			PlayerMP player = CraftingDead.instance().getLogicalServer().getPlayer(ctx.getServerHandler().player);
			player.handleTriggerStatusUpdate(msg.triggerDown);
			return null;
		}

	}

}
