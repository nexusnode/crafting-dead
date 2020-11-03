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
package com.craftingdead.core.commands.impl;

import com.craftingdead.core.commands.CommandBase;
import com.craftingdead.core.potion.ModEffects;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is a example of command using custom CommandBase class ({@link CommandBase});
 * Function: Cure Thirst
 */
public class CommandThirst extends CommandBase {

  private static final ITextComponent SUCCESS_MESSAGE_OPERATOR;
  private static final ITextComponent SUCCESS_MESSAGE_CONFIRMATION;
  private static final ITextComponent FAILURE_MESSAGE_NOT_USED;

  public CommandThirst() {
    super("thirst", null, 3);
  }

  @Override
  public void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher.register(Commands.literal(this.getName()).requires((source) -> {
        return source.hasPermissionLevel(this.getPermissions());
    }).executes((source) -> {
        return processCommand(source.getSource(), source.getSource().asPlayer().getGameProfile().getName());
    }).then(Commands.argument("target", StringArgumentType.string()).suggests((source, builder) -> {
        return ISuggestionProvider.suggest(source.getSource().getServer().getOnlinePlayerNames(), builder);
    }).executes((source) -> {
        return processCommand(source.getSource(), StringArgumentType.getString(source, "target"));
    })));
  }

  //========================//
  // Custom methods section
  //========================//

  /**
   * This method process how the command'll be performed by the server.
   *
   * @param source      - Source of command
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
        source.getServer().getPlayerList().getPlayers().stream().forEach(player -> thirst(source, player));
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
   * (Isn't needed a check to see i
   *
   * @param serverPlayerEntity
   */
  private static int thirst(final CommandSource source, ServerPlayerEntity serverPlayerEntity) {
    try {
      if (serverPlayerEntity.isAlive()) {
        serverPlayerEntity.addPotionEffect(new EffectInstance(ModEffects.HYDRATE.get(), 1, 20));
        serverPlayerEntity.playSound(SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.NEUTRAL, 5.0f, 5.0f);
        if (source.asPlayer() != serverPlayerEntity) {
          serverPlayerEntity.sendMessage(SUCCESS_MESSAGE_CONFIRMATION);
        }
        return 1;
      }
    } catch (CommandSyntaxException OK) {}
    return 0;
  }

  static {
    SUCCESS_MESSAGE_OPERATOR = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.thirst.success.operator").getString())
            .applyTextStyle(TextFormatting.GREEN);
    SUCCESS_MESSAGE_CONFIRMATION = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.thirst.success.confirmation").getString())
            .applyTextStyle(TextFormatting.GREEN);;
    FAILURE_MESSAGE_NOT_USED = new StringTextComponent(
        new TranslationTextComponent("commands.craftingdead.thirst.failure.not_used").getString())
            .applyTextStyle(TextFormatting.GRAY);;
  }
}
