package com.craftingdead.mod.network.message.server;

import com.craftingdead.mod.init.ModCapabilities;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetTriggerPressedSMessage implements IMessage {

	private int entityId;
	private boolean triggerPressed;

	public SetTriggerPressedSMessage() {
		;
	}

	public SetTriggerPressedSMessage(int entityId, boolean triggerPressed) {
		this.entityId = entityId;
		this.triggerPressed = triggerPressed;
	}

	@Override
	public void toBytes(ByteBuf out) {
		out.writeInt(entityId);
		out.writeBoolean(this.triggerPressed);
	}

	@Override
	public void fromBytes(ByteBuf in) {
		entityId = in.readInt();
		this.triggerPressed = in.readBoolean();
	}

	public static class SetTriggerPressedSHandler implements IMessageHandler<SetTriggerPressedSMessage, IMessage> {

		@Override
		public IMessage onMessage(SetTriggerPressedSMessage msg, MessageContext ctx) {
			Entity entity = Minecraft.getMinecraft().world.getEntityByID(msg.entityId);
			if (entity != null && entity instanceof AbstractClientPlayer)
				entity.getCapability(ModCapabilities.PLAYER, null).setTriggerPressed(msg.triggerPressed);
			return null;
		}

	}

}
