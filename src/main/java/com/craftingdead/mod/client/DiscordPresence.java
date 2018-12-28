package com.craftingdead.mod.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class DiscordPresence {

	private static final Logger LOGGER = LogManager.getLogger();

	private static boolean initialized;

	public static void initialize(String clientId) {
		if (initialized)
			return;
		DiscordRPC.discordInitialize(clientId, new DiscordEventHandlers(), true, null);
		initialized = true;
	}

	public static void updateState(GameState state, ClientDist client) {
		if (!initialized)
			return;
		DiscordRichPresence presence = new DiscordRichPresence();
		state.applyState(presence, client);
		DiscordRPC.discordUpdatePresence(presence);
	}

	public static void shutdown() {
		if (!initialized)
			return;
		DiscordRPC.discordShutdown();
	}

	public static enum GameState {

		PRE_INITIALIZATION("discordstate.pre-initialization"), INITIALIZATION("discordstate.initialization"),
		POST_INITIALIZATION("discordstate.post-initialization"), IDLE("discordstate.idle"),
		SINGLEPLAYER("discordstate.singleplayer"), LAN("discordstate.lan"), MULTIPLAYER("discordstate.multiplayer") {
			@Override
			public void applyState(DiscordRichPresence presence, ClientDist client) {
				ServerData serverData = client.getMinecraft().getCurrentServerData();
				presence.state = serverData.serverIP;
				String[] playerListSplit = serverData.populationInfo.split("/");
				try {
					presence.partySize = Integer
							.valueOf(TextFormatting.getTextWithoutFormattingCodes(playerListSplit[0]));
					presence.partyMax = Integer
							.valueOf(TextFormatting.getTextWithoutFormattingCodes(playerListSplit[1]));
				} catch (NumberFormatException e) {
					LOGGER.catching(e);
				}
			}
		};

		private String translationKey;

		private GameState(String translationKey) {
			this.translationKey = translationKey;
		}

		void applyState(DiscordRichPresence presence, ClientDist client) {
			presence.details = I18n.format(translationKey);
			presence.startTimestamp = System.currentTimeMillis() / 1000;
			presence.largeImageKey = "craftingdead";
			presence.largeImageText = "Crafting Dead";
		}
	}
}
