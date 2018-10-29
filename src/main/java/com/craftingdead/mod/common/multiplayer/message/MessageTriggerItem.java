package com.craftingdead.mod.common.multiplayer.message;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.item.TriggerableItem;
import com.craftingdead.mod.common.multiplayer.PlayerMP;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTriggerItem implements IMessage {

	private boolean triggerDown;

	public MessageTriggerItem() {
		;
	}

	public MessageTriggerItem(boolean triggerDown) {
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

	public static class MessageHandlerItemTrigger implements IMessageHandler<MessageTriggerItem, IMessage> {

		@Override
		public IMessage onMessage(MessageTriggerItem msg, MessageContext ctx) {
			PlayerMP player = CraftingDead.instance().getLogicalServer().getPlayer(ctx.getServerHandler().player);
			ItemStack heldStack = player.getVanillaEntity().getHeldItemMainhand();
			// Never trust the client, they may not actually be holding a TriggerableItem
			if (heldStack.getItem() instanceof TriggerableItem)
				((TriggerableItem) heldStack.getItem()).handleTrigger(player, heldStack, msg.triggerDown);
			return null;
		}

	}

}
