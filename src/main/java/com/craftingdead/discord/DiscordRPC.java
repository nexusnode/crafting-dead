package com.craftingdead.discord;

import com.sun.jna.Library;
import com.sun.jna.Native;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DiscordRPC extends Library {
    DiscordRPC INSTANCE = Native.loadLibrary("discord-rpc", DiscordRPC.class);

    int DISCORD_REPLY_NO = 0;
    int DISCORD_REPLY_YES = 1;
    int DISCORD_REPLY_IGNORE = 2;

    void Discord_Initialize(@Nonnull String applicationId, @Nullable DiscordEventHandlers handlers, boolean autoRegister, @Nullable String steamId);

    void Discord_Shutdown();

    void Discord_RunCallbacks();

    void Discord_UpdateConnection();

    void Discord_UpdatePresence(@Nullable DiscordRichPresence struct);

    void Discord_ClearPresence();

    void Discord_Respond(@Nonnull String userid, int reply);

    void Discord_UpdateHandlers(@Nullable DiscordEventHandlers handlers);

    void Discord_Register(String applicationId, String command);

    void Discord_RegisterSteamGame(String applicationId, String steamId);
}
