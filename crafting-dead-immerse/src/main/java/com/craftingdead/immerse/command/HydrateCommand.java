package com.craftingdead.immerse.command;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.Permissions;
import com.craftingdead.immerse.game.survival.SurvivalGame;
import com.craftingdead.immerse.game.survival.SurvivalPlayerHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.fml.LogicalSide;

public class HydrateCommand {

  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("hydrate")
        .requires(source -> ImmerseCommands.hasPermission(source, Permissions.HYDRATE)
            && CraftingDeadImmerse.getInstance()
                .getGame(LogicalSide.SERVER) instanceof SurvivalGame game
            && game.isThirstEnabled())
        .executes(context -> processCommand(context.getSource(),
            List.of(context.getSource().getPlayerOrException())))
        .then(Commands.argument("targets", EntityArgument.entities())
            .executes(context -> processCommand(context.getSource(),
                EntityArgument.getPlayers(context, "targets")))));
  }

  private static int processCommand(CommandSourceStack source, Collection<ServerPlayer> targets) {
    var hydrated = targets.stream()
        .filter(ServerPlayer::isAlive)
        .flatMap(player -> Stream.ofNullable(PlayerExtension.get(player)))
        .peek(HydrateCommand::hydrate)
        .toList();

    source.sendSuccess(
        hydrated.size() == 1
            ? new TranslatableComponent("commands.hydrate.success.single",
                hydrated.get(0).getEntity().getDisplayName())
                    .withStyle(ChatFormatting.GREEN)
            : new TranslatableComponent("commands.hydrate.success.multiple", hydrated.size())
                .withStyle(ChatFormatting.GREEN),
        false);

    return 0;
  }

  private static void hydrate(PlayerExtension<ServerPlayer> player) {
    player.getHandlerOrThrow(SurvivalPlayerHandler.TYPE).setWater(20);
    player.getEntity().playNotifySound(SoundEvents.GENERIC_DRINK, SoundSource.NEUTRAL, 5.0F, 5.0F);
  }
}
