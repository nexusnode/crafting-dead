package com.craftingdead.mod.client.network;

import java.util.ArrayList;
import java.util.List;

import com.craftingdead.mod.client.ClientMod;
import com.craftingdead.mod.common.CraftingDead;
import com.recastproductions.network.impl.Session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class SessionModClient extends Session<NetClientHandlerModClient> {

	private final ClientMod modClient;

	private List<String> news = new ArrayList<String>();

	public SessionModClient(Channel ch, NetClientHandlerModClient handler, ClientMod modClient) {
		super(ch, handler);
		this.modClient = modClient;
		CraftingDead.LOGGER.info("Connected to the Crafting Dead network");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		CraftingDead.LOGGER.info("Disconnected from the Crafting Dead network");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
		CraftingDead.LOGGER.warn("Exception in session", t);
	}

	public void setNews(List<String> news) {
		news = new ArrayList<String>(news);
	}

	public List<String> getNews() {
		return this.news;
	}

	public ClientMod getModClient() {
		return this.modClient;
	}

}
