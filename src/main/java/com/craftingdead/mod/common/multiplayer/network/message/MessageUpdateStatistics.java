package com.craftingdead.mod.common.multiplayer.network.message;

import com.craftingdead.mod.client.multiplayer.PlayerSP;

import io.netty.buffer.ByteBuf;
import sm0keysa1m0n.network.message.Message;

public class MessageUpdateStatistics implements Message {

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
	public void toBytes(ByteBuf out) throws Exception {
		out.writeInt(daysSurvived);
		out.writeInt(zombieKills);
		out.writeInt(playerKills);
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		daysSurvived = in.readInt();
		zombieKills = in.readInt();
		playerKills = in.readInt();
	}

	public static class MessageHandlerUpdateStatistics implements MessageHandler<MessageUpdateStatistics, Message> {

		@Override
		public Message processMessage(MessageUpdateStatistics msg, MessageContext ctx) {
			PlayerSP player = ctx.getMod().getModClient().getPlayer();
			player.setDaysSurvived(msg.daysSurvived);
			player.setZombieKills(msg.zombieKills);
			player.setPlayerKills(msg.playerKills);
			return null;
		}

	}

}
