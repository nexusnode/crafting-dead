package com.craftingdead.mod.common.network.packet;

import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;

import io.netty.buffer.ByteBuf;

public class PacketUpdateStatistics implements IPacket {

	private int daysSurvived;

	public PacketUpdateStatistics() {
		;
	}

	public PacketUpdateStatistics(int daysSurvived) {
		this.daysSurvived = daysSurvived;
	}

	@Override
	public void toBytes(ByteBuf out) throws Exception {
		out.writeInt(daysSurvived);
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		daysSurvived = in.readInt();
	}

	public static class PacketHandlerUpdateStatistics
			implements IPacketHandler<PacketUpdateStatistics, IPacket, PacketContextMod> {

		@Override
		public IPacket processPacket(PacketUpdateStatistics packet, PacketContextMod ctx) {
			if (ctx.getModClient().getPlayer() != null) {
				ctx.getModClient().getPlayer().setDaysSurvived(packet.daysSurvived);
			}
			return null;
		}

	}

}
