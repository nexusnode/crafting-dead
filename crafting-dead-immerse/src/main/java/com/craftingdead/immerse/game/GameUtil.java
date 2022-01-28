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

package com.craftingdead.immerse.game;

import org.apache.commons.lang3.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class GameUtil {

  private static final String SQUARE_UTF_8 = "\u2588";
  private static final Component CHAT_SEPERATOR =
      new TextComponent(StringUtils.repeat(SQUARE_UTF_8, 30));
  public static final Component NEW_LINE = new TextComponent("\n\n");

  public static Component formatMessage(Component message) {
    return new TranslatableComponent("message.game")
        .withStyle(ChatFormatting.AQUA)
        .append(new TextComponent(" >>> ")
            .withStyle(style -> style
                .withBold(true)
                .withColor(TextColor.fromRgb(0xFFFFFF))))
        .append(message.copy().withStyle(ChatFormatting.GRAY));
  }

  public static void sendGameMessageToAll(Component message, MinecraftServer minecraftServer) {
    minecraftServer.getPlayerList().broadcastMessage(formatMessage(message), ChatType.SYSTEM,
        Util.NIL_UUID);
  }

  public static void sendAnnouncement(Component message, MinecraftServer minecraftServer) {
    minecraftServer.getPlayerList().broadcastAll(new ClientboundSetTitleTextPacket(message));
  }

  public static void sendChatAnnouncement(Component title, Component body,
      MinecraftServer minecraftServer) {
    final Component announcement = CHAT_SEPERATOR.copy()
        .append(NEW_LINE)
        .append(title.copy().withStyle(Style.EMPTY.withBold(true)))
        .append(NEW_LINE)
        .append(body)
        .append(NEW_LINE)
        .append(CHAT_SEPERATOR);
    minecraftServer.getPlayerList().broadcastMessage(announcement, ChatType.SYSTEM,
        Util.NIL_UUID);
  }

  public static void broadcastSound(SoundEvent soundEvent, MinecraftServer minecraftServer) {
    broadcastSound(soundEvent, minecraftServer, 0.7F, 1.0F);
  }

  public static void broadcastSound(SoundEvent soundEvent, MinecraftServer minecraftServer,
      float volume, float pitch) {
    minecraftServer.getPlayerList().getPlayers()
        .forEach(playerEntity -> playerEntity.playNotifySound(soundEvent,
            SoundSource.MASTER, volume, pitch));
  }
}
