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
