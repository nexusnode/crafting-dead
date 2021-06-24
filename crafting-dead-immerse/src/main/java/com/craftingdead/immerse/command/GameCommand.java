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

package com.craftingdead.immerse.command;

import com.craftingdead.immerse.CraftingDeadImmerse;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class GameCommand {

  private static int restart(CommandContext<CommandSource> context) {
    context.getSource().sendSuccess(
        new TranslationTextComponent("commands.game.restart"), true);
    CraftingDeadImmerse.getInstance().getLogicalServer().restartGame();
    return 0;
  }

  private static int reloadRotation(CommandContext<CommandSource> context) {
    context.getSource().sendSuccess(
        new TranslationTextComponent("commands.game.reload_rotation"), true);
    CraftingDeadImmerse.getInstance().getLogicalServer().reloadGameRotation();
    return 0;
  }

  public static void register(CommandDispatcher<CommandSource> dispatcher) {
    dispatcher
        .register(Commands.literal("game")
            .requires(context -> context.hasPermission(2))
            .then(Commands.literal("restart").executes(GameCommand::restart))
            .then(Commands.literal("reload_rotation").executes(GameCommand::reloadRotation)));
  }
}
