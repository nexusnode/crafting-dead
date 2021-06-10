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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.game.Game;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.module.Module;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class SurvivalGame<M extends Module> implements Game<M> {

  @Override
  public void load() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void unload() {
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  @Override
  public void tick() {}

  @SubscribeEvent
  public void handleLivingExtensionLoad(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension
        && !event.getLiving().getHandler(SurvivalPlayerHandler.EXTENSION_ID).isPresent()) {
      PlayerExtension<?> player = (PlayerExtension<?>) event.getLiving();
      player.registerHandler(SurvivalPlayerHandler.EXTENSION_ID, new SurvivalPlayerHandler(player));
    }
  }

  @Override
  public GameType getType() {
    return GameTypes.SURVIVAL.get();
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {}

  @Override
  public void decode(PacketBuffer in) {}

  @Override
  public boolean requiresSync() {
    return false;
  }
}
