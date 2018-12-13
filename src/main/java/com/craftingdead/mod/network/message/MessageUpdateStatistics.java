package com.craftingdead.mod.network.message;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.player.ClientPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateStatistics implements IMessage {

	private int daysSurvived;
	private int zombieKills;
	private int playerKills;

	public MessageUpdateStatistics() {
		;
	}

	public MessageUpdateStatistics(int daysSurvived, int zombieKills, int playerKills) {
		this.daysSurvived = daysSurvived;
		this.zombieKills = zombieKills;
		this.playerKills = playerKills;
	}

	@Override
	public void toBytes(ByteBuf out) {
		out.writeInt(this.daysSurvived);
		out.writeInt(this.zombieKills);
		out.writeInt(this.playerKills);
	}

	@Override
	public void fromBytes(ByteBuf in) {
		this.daysSurvived = in.readInt();
		this.zombieKills = in.readInt();
		this.playerKills = in.readInt();
	}

	public static class MessageHandlerUpdateStatistics implements IMessageHandler<MessageUpdateStatistics, IMessage> {

		@Override
		public IMessage onMessage(MessageUpdateStatistics msg, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				ClientPlayer player = CraftingDead.instance().getMod().getClientMod().getPlayer();
				player.updateStatistics(msg.daysSurvived, msg.zombieKills, msg.playerKills);
			});

			return null;
		}

	}

}
