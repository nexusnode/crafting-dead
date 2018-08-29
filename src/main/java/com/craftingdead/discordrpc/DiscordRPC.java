package com.craftingdead.discordrpc;

import com.google.common.collect.ImmutableMap;
import com.sun.jna.Library;
import com.sun.jna.Native;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DiscordRPC extends Library {

	DiscordRPC INSTANCE = Native.loadLibrary("discord-rpc", DiscordRPC.class,
			ImmutableMap.of(Library.OPTION_FUNCTION_MAPPER, new DiscordRPCMapper()));

	int DISCORD_REPLY_NO = 0;
	int DISCORD_REPLY_YES = 1;
	int DISCORD_REPLY_IGNORE = 2;

	void discordInitialize(@Nonnull String applicationId, @Nullable DiscordEventHandlers handlers, boolean autoRegister,
			@Nullable String steamId);

	void discordShutdown();

	void discordRunCallbacks();

	void discordUpdateConnection();

	void discordUpdatePresence(@Nullable DiscordRichPresence struct);

	void discordClearPresence();

	void discordRespond(@Nonnull String userid, int reply);

	void discordUpdateHandlers(@Nullable DiscordEventHandlers handlers);

	void discordRegister(String applicationId, String command);

	void discordRegisterSteamGame(String applicationId, String steamId);
}
