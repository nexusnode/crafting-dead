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
