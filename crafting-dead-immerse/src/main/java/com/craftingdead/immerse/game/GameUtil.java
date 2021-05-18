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
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class GameUtil {

  private static final String SQUARE_UTF_8 = "\u2588";
  private static final ITextComponent CHAT_SEPERATOR =
      new StringTextComponent(StringUtils.repeat(SQUARE_UTF_8, 30));
  public static final ITextComponent NEW_LINE = new StringTextComponent("\n\n");

  public static ITextComponent formatMessage(ITextComponent message) {
    return new TranslationTextComponent("message.game")
        .withStyle(TextFormatting.AQUA)
        .append(new StringTextComponent(" >>> ")
            .withStyle(style -> style
                .withBold(true)
                .withColor(Color.fromRgb(0xFFFFFF))))
        .append(message.copy().withStyle(TextFormatting.GRAY));
  }

  public static void sendGameMessageToAll(ITextComponent message, MinecraftServer minecraftServer) {
    minecraftServer.getPlayerList().broadcastMessage(formatMessage(message), ChatType.SYSTEM,
        Util.NIL_UUID);
  }

  public static void sendAnnouncement(ITextComponent message, MinecraftServer minecraftServer) {
    minecraftServer.getPlayerList()
        .broadcastAll(new STitlePacket(STitlePacket.Type.TITLE, message));
  }

  public static void sendChatAnnouncement(ITextComponent title, ITextComponent body,
      MinecraftServer minecraftServer) {
    final ITextComponent announcement = CHAT_SEPERATOR.copy()
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
            SoundCategory.MASTER, volume, pitch));
  }
}
