package com.craftingdead.network.packet.modclient.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.network.modclient.SessionModClient;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;

public class SPacketNews implements IPacket {

	private List<String> news;

	public SPacketNews() {
		;
	}

	public SPacketNews(List<String> news) {
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
	
	public static class PacketHandlerSPacketNews implements IPacketHandler<SPacketNews, IPacket, SessionModClient> {

		@Override
		public IPacket processPacket(SPacketNews packet, SessionModClient session) {
			session.setNews(packet.news);
			CraftingDead.LOGGER.info("News successfully updated");
			return null;
		}
		
	}

}
