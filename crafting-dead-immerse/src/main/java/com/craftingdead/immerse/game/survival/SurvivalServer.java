/*
 * Crafting Dead Copyright (C) 2021 NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
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
