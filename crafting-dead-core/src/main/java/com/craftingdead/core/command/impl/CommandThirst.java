/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.command.impl;

import com.craftingdead.core.capability.living.IPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.concurrent.atomic.AtomicBoolean;

public class CommandThirst {

  private static final ITextComponent SUCCESS_MESSAGE_OPERATOR;
  private static final ITextComponent SUCCESS_MESSAGE_CONFIRMATION;
  private static final ITextComponent FAILURE_MESSAGE_NOT_USED;

  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("thirst").requires((source) -> {
      return source.hasPermissionLevel(3);
    }).executes((source) -> {
      return processCommand(source.getSource(),
          source.getSource().asPlayer().getGameProfile().getName());
    }).then(Commands.argument("target", StringArgumentType.string()).suggests((source, builder) -> {
      return ISuggestionProvider.suggest(source.getSource().getServer().getOnlinePlayerNames(),
          builder);
    }).executes((source) -> {
      return processCommand(source.getSource(), StringArgumentType.getString(source, "target"));
    })));
  }

  /**
   * This method process how the command'll be performed by the server.
   *
   * @param source - Source of command
   * @param player_name - Name of player to be thirsted (nullable)
   * @return
   */
  private static int processCommand(final CommandSource source, final String player_name) {
    AtomicBoolean utilized = new AtomicBoolean(false);
    if (player_name != null && !player_name.isEmpty()) {
      if (!player_name.equals("all") && !player_name.equals("*")) {
        source.getServer().getPlayerList().getPlayers().stream().filter(player -> {
          return player.getGameProfile().getName().equalsIgnoreCase(player_name);
        }).findFirst().ifPresent(player -> {
          thirst(source, player);
          utilized.set(true);
        });
      } else {
        source.getServer().getPlayerList().getPlayers().stream()
            .forEach(player -> thirst(source, player));
        utilized.set(true);
      }
    }
    if (utilized.get()) {
      source.sendFeedback(SUCCESS_MESSAGE_OPERATOR, true);
    } else {
      source.sendFeedback(FAILURE_MESSAGE_NOT_USED, true);
    }
    return 1;
  }

  /**
   * This method provides Thirst function in command.
   *
   * @param source - Command executor
   * @param serverPlayerEntity - Entity to be thirsted
   */
  private static int thirst(final CommandSource source, ServerPlayerEntity serverPlayerEntity) {
    try {
      if (serverPlayerEntity.isAlive()) {
        IPlayer.getExpected(serverPlayerEntity).setWater(20);
        serverPlayerEntity.playSound(SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.NEUTRAL, 5.0f,
            5.0f);
        if (source.asPlayer() != serverPlayerEntity) {
          serverPlayerEntity.sendMessage(SUCCESS_MESSAGE_CONFIRMATION, Util.DUMMY_UUID);
        }
        return 1;
      }
    } catch (CommandSyntaxException OK) {
    }
    return 0;
  }

  static {
    SUCCESS_MESSAGE_OPERATOR = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.thirst.success.operator").getString())
            .mergeStyle(TextFormatting.GREEN);
    SUCCESS_MESSAGE_CONFIRMATION = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.thirst.success.confirmation")
            .getString()).mergeStyle(TextFormatting.GREEN);;
    FAILURE_MESSAGE_NOT_USED = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.thirst.failure.not_used").getString())
            .mergeStyle(TextFormatting.GRAY);;
  }
}
