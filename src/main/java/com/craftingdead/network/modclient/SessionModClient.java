package com.craftingdead.network.modclient;

import java.util.ArrayList;
import java.util.List;

import com.craftingdead.mod.client.ModClient;
import com.craftingdead.mod.core.CraftingDead;
import com.recastproductions.network.Session;

import io.netty.channel.Channel;

public class SessionModClient extends Session<NetClientHandlerModClient> {
	
	private final ModClient modClient;
	
	private List<String> news = new ArrayList<String>();

	public SessionModClient(Channel ch, NetClientHandlerModClient handler, ModClient modClient) {
		super(ch, handler);
		this.modClient = modClient;
		CraftingDead.LOGGER.info("Connected to the Crafting Dead Network!");
	}
	
	public void setNews(List<String> news) {
		news = new ArrayList<String>(news);
	}
	
	public List<String> getNews() {
		return this.news;
	}

}
