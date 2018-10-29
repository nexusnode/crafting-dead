package com.craftingdead.mod.common.multiplayer.message;

import com.craftingdead.mod.client.multiplayer.PlayerSP;
import com.craftingdead.mod.common.CraftingDead;

import io.netty.buffer.ByteBuf;
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
		out.writeInt(daysSurvived);
		out.writeInt(zombieKills);
		out.writeInt(playerKills);
	}

	@Override
	public void fromBytes(ByteBuf in) {
		daysSurvived = in.readInt();
		zombieKills = in.readInt();
		playerKills = in.readInt();
	}

	public static class MessageHandlerUpdateStatistics implements IMessageHandler<MessageUpdateStatistics, IMessage> {

		@Override
		public IMessage onMessage(MessageUpdateStatistics msg, MessageContext ctx) {
			PlayerSP player = CraftingDead.instance().getProxy().getModClient().getPlayer();
			if (player != null) {
				player.setDaysSurvived(msg.daysSurvived);
				player.setZombieKills(msg.zombieKills);
				player.setPlayerKills(msg.playerKills);
			}
			return null;
		}

	}

}
