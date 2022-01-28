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
import com.craftingdead.immerse.Permissions;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.server.permission.PermissionAPI;

public class GameCommand {

  private static int restart(CommandContext<CommandSourceStack> context) {
    context.getSource().sendSuccess(
        new TranslatableComponent("commands.game.restart"), true);
    CraftingDeadImmerse.getInstance().getLogicalServer().restartGame();
    return 0;
  }

  private static int reloadRotation(CommandContext<CommandSourceStack> context) {
    context.getSource().sendSuccess(
        new TranslatableComponent("commands.game.reload_rotation"), true);
    CraftingDeadImmerse.getInstance().getLogicalServer().reloadGameRotation();
    return 0;
  }

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher
        .register(Commands.literal("game")
            .requires(context -> context.hasPermission(4)
                || (context.getEntity()instanceof ServerPlayer player
                    && PermissionAPI.getPermission(player, Permissions.GAME_OP)))
            .then(Commands.literal("restart").executes(GameCommand::restart))
            .then(Commands.literal("reload_rotation").executes(GameCommand::reloadRotation)));
  }
}
