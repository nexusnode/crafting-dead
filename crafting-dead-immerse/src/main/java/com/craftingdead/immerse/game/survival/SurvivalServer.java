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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.GameServer;
import com.craftingdead.immerse.world.action.BuildBlockAction;
import com.craftingdead.immerse.world.level.block.ImmerseBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SurvivalServer extends SurvivalGame implements GameServer {

  @Override
  public boolean persistPlayerData() {
    return true;
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean persistGameData() {
    return true;
  }

  @Override
  public void addPlayer(PlayerExtension<ServerPlayer> player) {}

  @Override
  public void removePlayer(PlayerExtension<ServerPlayer> player) {}

  @SubscribeEvent
  public void handlePerformAction(LivingExtensionEvent.PerformAction<BuildBlockAction> event) {
    if (!event.getLiving().getLevel().isClientSide()
        && event.getAction().getType().getBlock() == ImmerseBlocks.BASE_CENTER.get()
        && event.getLiving() instanceof PlayerExtension<?> player) {
      var handler = player.getHandlerOrThrow(SurvivalPlayerHandler.TYPE);
      handler.getBasePos().ifPresent(pos -> {
        event.setCanceled(true);
        player.getEntity().displayClientMessage(
            new TranslatableComponent("message.already_own_base", pos.toShortString())
                .withStyle(ChatFormatting.RED),
            true);
      });
    }
  }
}
