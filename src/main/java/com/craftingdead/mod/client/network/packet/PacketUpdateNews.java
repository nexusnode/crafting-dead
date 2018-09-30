package com.craftingdead.mod.client.network.packet;

import com.craftingdead.mod.client.network.SessionModClient;
import com.craftingdead.mod.common.CraftingDead;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import com.recastproductions.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketUpdateNews implements IPacket {

	private List<String> news;

	public PacketUpdateNews() {
		;
	}

	public PacketUpdateNews(List<String> news) {
		this.news = new ArrayList<String>(news);
	}

	@Override
	public void toBytes(ByteBuf out) throws Exception {
		ByteBufUtils.writeStringArray(out, news.toArray(new String[0]));
	}

	@Override
	public void fromBytes(ByteBuf in) throws Exception {
		news = Arrays.asList(ByteBufUtils.readStringArray(in));
	}

	public static class PacketHandlerUpdateNews implements IPacketHandler<PacketUpdateNews, IPacket, SessionModClient> {

		@Override
		public IPacket processPacket(PacketUpdateNews packet, SessionModClient session) {
			session.setNews(packet.news);
			CraftingDead.LOGGER.info("News successfully updated");
			return null;
		}

	}

}
