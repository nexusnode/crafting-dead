package com.craftingdead.mod.common.multiplayer.network.packet;

import com.craftingdead.mod.client.multiplayer.PlayerSP;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;

import io.netty.buffer.ByteBuf;

public class PacketUpdateStatistics implements IPacket {

	private int daysSurvived;
	private int zombieKills;
	private int playerKills;

	public PacketUpdateStatistics() {
		;
	}

	public PacketUpdateStatistics(int daysSurvived, int zombieKills, int playerKills) {
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

	public static class PacketHandlerUpdateStatistics
			implements IPacketHandler<PacketUpdateStatistics, IPacket, PacketContextMod> {

		@Override
		public IPacket processPacket(PacketUpdateStatistics packet, PacketContextMod ctx) {
			PlayerSP player = ctx.getMod().getModClient().getPlayer();
			player.setDaysSurvived(packet.daysSurvived);
			player.setZombieKills(packet.zombieKills);
			player.setPlayerKills(packet.playerKills);
			return null;
		}

	}

}
