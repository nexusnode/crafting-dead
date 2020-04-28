package com.craftingdead.immerse.client;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.resources.I18n;

public class DiscordPresence {

  private static boolean initialized;

  public static void initialize(String clientId) {
    if (initialized) {
      return;
    }
    DiscordRPC.discordInitialize(clientId, new DiscordEventHandlers(), true, null);
    initialized = true;
  }

  public static void updateState(GameState state, ClientDist client) {
    if (!initialized) {
      return;
    }
    DiscordRichPresence presence = new DiscordRichPresence();
    state.applyState(presence, client);
    DiscordRPC.discordUpdatePresence(presence);
  }

  public static void shutdown() {
    if (!initialized) {
      return;
    }
    DiscordRPC.discordShutdown();
  }

  public static enum GameState {

    IDLE, SINGLEPLAYER("title.singleplayer"), REALMS(
        "title.multiplayer.realms"), LAN(
            "title.multiplayer.lan"), MULTIPLAYER("title.multiplayer.other");

    private String translationKey;

    private GameState() {}

    private GameState(String translationKey) {
      this.translationKey = translationKey;
    }

    public void applyState(DiscordRichPresence presence, ClientDist client) {
      if (this.translationKey != null) {
        presence.details = I18n.format(this.translationKey);
      }
      presence.startTimestamp = System.currentTimeMillis() / 1000;
      presence.largeImageKey = "craftingdead";
      presence.largeImageText = "Crafting Dead";
    }
  }
}
