/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
