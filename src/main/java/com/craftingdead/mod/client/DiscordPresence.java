package com.craftingdead.mod.client;

import com.craftingdead.mod.common.CraftingDead;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class DiscordPresence {

	public static void initialize(String clientId) {
		DiscordRPC.discordInitialize(clientId, new DiscordEventHandlers(), true, null);
	}

	public static void updateState(GameState state, ModClient mod) {
		DiscordRichPresence presence = new DiscordRichPresence();
		state.applyState(presence, mod);
		DiscordRPC.discordUpdatePresence(presence);
	}

	public static void shutdown() {
		DiscordRPC.discordShutdown();
	}

	public static enum GameState {

		PRE_INITIALIZATION("discordstate.pre-initialization"), INITIALIZATION("discordstate.initialization"),
		POST_INITIALIZATION("discordstate.post-initialization"), IDLE("discordstate.idle"),
		SINGLEPLAYER("discordstate.singleplayer"), MULTIPLAYER("discordstate.multiplayer") {
			@Override
			public void applyState(DiscordRichPresence presence, ModClient mod) {
				ServerData serverData = mod.getMinecraft().getCurrentServerData();
				presence.state = serverData.serverIP;
				String[] playerListSplit = serverData.populationInfo.split("/");
				try {
					presence.partySize = Integer
							.valueOf(TextFormatting.getTextWithoutFormattingCodes(playerListSplit[0]));
					presence.partyMax = Integer
							.valueOf(TextFormatting.getTextWithoutFormattingCodes(playerListSplit[1]));
				} catch (NumberFormatException e) {
					CraftingDead.LOGGER.catching(e);
				}
			}
		};

		private String translationKey;

		private GameState(String translationKey) {
			this.translationKey = translationKey;
		}

		void applyState(DiscordRichPresence presence, ModClient mod) {
			presence.details = I18n.format(translationKey);
			presence.startTimestamp = System.currentTimeMillis() / 1000;
			presence.largeImageKey = "craftingdead";
			presence.largeImageText = "Crafting Dead";
		}

	}

}
