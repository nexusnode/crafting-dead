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

package com.craftingdead.core.commands.impl;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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

public class HydrateCommand {

  private static final ITextComponent SUCCESS_MESSAGE_OPERATOR;
  private static final ITextComponent SUCCESS_MESSAGE_CONFIRMATION;
  private static final ITextComponent FAILURE_MESSAGE_NOT_USED;

  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal("hydrate").requires((source) -> {
      return source.hasPermission(2);
    }).executes((source) -> {
      return processCommand(source.getSource(),
          source.getSource().getPlayerOrException().getGameProfile().getName());
    }).then(Commands.argument("target", StringArgumentType.string()).suggests((source, builder) -> {
      return ISuggestionProvider.suggest(source.getSource().getServer().getPlayerNames(),
          builder);
    }).executes((source) -> {
      return processCommand(source.getSource(), StringArgumentType.getString(source, "target"));
    })));
  }

  /**
   * This method process how the command'll be performed by the server.
   *
   * @param source - Source of command
   * @param username - Name of player to be thirsted (nullable)
   * @return
   */
  private static int processCommand(final CommandSource source, final String username) {
    int result = -1;
    if (username != null && !username.isEmpty()) {
      boolean all = username.equals("all") || username.equals("*");
      result = source.getServer().getPlayerList().getPlayers()
          .stream()
          .filter(player -> all || player.getGameProfile().getName().equalsIgnoreCase(username))
          .findFirst()
          .map(HydrateCommand::hydrate)
          .orElse(-1);
    }
    if (result == 1) {
      source.sendSuccess(SUCCESS_MESSAGE_OPERATOR, false);
    } else {
      source.sendFailure(FAILURE_MESSAGE_NOT_USED);
    }
    return result;
  }

  /**
   * Hydrate the specified player.
   *
   * @param serverPlayerEntity - entity to be hydrated
   * @return 1 if success or 0 if failed
   */
  private static int hydrate(ServerPlayerEntity serverPlayerEntity) {
    if (serverPlayerEntity.isAlive()) {
      PlayerExtension.getExpected(serverPlayerEntity).setWater(20);
      serverPlayerEntity.playNotifySound(SoundEvents.GENERIC_DRINK, SoundCategory.NEUTRAL, 5.0F,
          5.0F);
      serverPlayerEntity.sendMessage(SUCCESS_MESSAGE_CONFIRMATION, Util.NIL_UUID);
      return 1;
    }
    return 0;
  }

  static {
    SUCCESS_MESSAGE_OPERATOR = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.hydrate.success.operator").getString())
            .withStyle(TextFormatting.GREEN);
    SUCCESS_MESSAGE_CONFIRMATION = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.hydrate.success.confirmation")
            .getString()).withStyle(TextFormatting.GREEN);
    FAILURE_MESSAGE_NOT_USED = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.hydrate.failure.not_used").getString())
            .withStyle(TextFormatting.GRAY);
  }
}
