/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.immerse.client;

//import net.arikia.dev.drpc.DiscordEventHandlers;
//import net.arikia.dev.drpc.DiscordRPC;
//import net.arikia.dev.drpc.DiscordRichPresence;
//import net.minecraft.client.resources.I18n;
//
//public class DiscordPresence {
//
//  private static boolean initialized;
//
//  public static void initialize(String clientId) {
//    if (initialized) {
//      return;
//    }
//    DiscordRPC.discordInitialize(clientId, new DiscordEventHandlers(), true, null);
//    initialized = true;
//  }
//
//  public static void updateState(GameState state, ClientDist client) {
//    if (!initialized) {
//      return;
//    }
//    DiscordRichPresence presence = new DiscordRichPresence();
//    state.applyState(presence, client);
//    DiscordRPC.discordUpdatePresence(presence);
//  }
//
//  public static void shutdown() {
//    if (!initialized) {
//      return;
//    }
//    DiscordRPC.discordShutdown();
//  }
//
//  public static enum GameState {
//
//    IDLE, SINGLEPLAYER("title.singleplayer"), REALMS(
//        "title.multiplayer.realms"), LAN(
//            "title.multiplayer.lan"), MULTIPLAYER("title.multiplayer.other");
//
//    private String translationKey;
//
//    private GameState() {}
//
//    private GameState(String translationKey) {
//      this.translationKey = translationKey;
//    }
//
//    public void applyState(DiscordRichPresence presence, ClientDist client) {
//      if (this.translationKey != null) {
//        presence.details = I18n.format(this.translationKey);
//      }
//      presence.startTimestamp = System.currentTimeMillis() / 1000;
//      presence.largeImageKey = "craftingdead";
//      presence.largeImageText = "Crafting Dead";
//    }
//  }
//}
