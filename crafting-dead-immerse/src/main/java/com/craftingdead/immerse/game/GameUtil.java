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
import com.craftingdead.core.util.Text;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class GameUtil {

  private static final String SQUARE_UTF_8 = "\u2588";
  private static final ITextComponent CHAT_SEPERATOR =
      Text.of(StringUtils.repeat(SQUARE_UTF_8, 30));
  public static final ITextComponent NEW_LINE = Text.of("\n\n");

  public static ITextComponent formatMessage(ITextComponent message) {
    // @formatter:off
    return Text.translate("message.game").mergeStyle(TextFormatting.AQUA)
        .append(Text.of(" >>> ")
            .setStyle(Style.EMPTY
                .setBold(true)
                .setColor(Color.fromInt(0xFFFFFF))))
        .append(message.copyRaw().mergeStyle(TextFormatting.GRAY));
    // @formatter:on
  }

  public static void sendGameMessageToAll(ITextComponent message, MinecraftServer minecraftServer) {
    minecraftServer.getPlayerList().func_232641_a_(formatMessage(message), ChatType.SYSTEM,
        Util.DUMMY_UUID);
  }

  public static void sendAnnouncement(ITextComponent message, MinecraftServer minecraftServer) {
    minecraftServer.getPlayerList()
        .sendPacketToAllPlayers(new STitlePacket(STitlePacket.Type.TITLE, message));
  }

  public static void sendChatAnnouncement(ITextComponent title, ITextComponent body,
      MinecraftServer minecraftServer) {
    // @formatter:off
    final ITextComponent announcement = CHAT_SEPERATOR.copyRaw()
        .append(NEW_LINE)
        .append(title.copyRaw().mergeStyle(Style.EMPTY.setBold(true)))
        .append(NEW_LINE)
        .append(body)
        .append(NEW_LINE)
        .append(CHAT_SEPERATOR);
    // @formatter:on
    minecraftServer.getPlayerList().func_232641_a_(announcement, ChatType.SYSTEM,
        Util.DUMMY_UUID);
  }

  public static void broadcastSound(SoundEvent soundEvent, MinecraftServer minecraftServer) {
    broadcastSound(soundEvent, minecraftServer, 0.7F, 1.0F);
  }

  public static void broadcastSound(SoundEvent soundEvent, MinecraftServer minecraftServer,
      float volume, float pitch) {
    minecraftServer.getPlayerList().getPlayers()
        .forEach(playerEntity -> playerEntity.playSound(soundEvent,
            SoundCategory.MASTER, volume, pitch));
  }
}
