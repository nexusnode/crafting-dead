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

import com.craftingdead.core.capability.living.PlayerImpl;
import com.craftingdead.core.event.LivingEvent;
import com.craftingdead.immerse.game.GameType;
import com.craftingdead.immerse.game.GameTypes;
import com.craftingdead.immerse.game.IGame;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SurvivalGame implements IGame {

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
  public void handleAttachLivingExtensions(LivingEvent.Load event) {
    if (event.getLiving() instanceof PlayerImpl
        && !event.getLiving().getExtension(SurvivalPlayer.EXTENSION_ID).isPresent()) {
      PlayerImpl<?> player = (PlayerImpl<?>) event.getLiving();
      player.registerExtension(SurvivalPlayer.EXTENSION_ID, new SurvivalPlayer(player));
    }
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {}

  @Override
  public void decode(PacketBuffer in) {}

  @Override
  public boolean requiresSync() {
    return false;
  }

  @Override
  public GameType getGameType() {
    return GameTypes.SURVIVAL.get();
  }
}
