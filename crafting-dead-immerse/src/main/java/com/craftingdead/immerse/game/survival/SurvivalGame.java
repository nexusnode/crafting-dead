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
package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.immerse.game.AbstractGame;
import com.craftingdead.immerse.game.GameTypes;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SurvivalGame extends AbstractGame<SurvivorsTeam> {

  public SurvivalGame() {
    super(GameTypes.SURVIVAL, ImmutableSet.of(new SurvivorsTeam()), "Survival");
  }

  @Override
  public void load() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public void unload() {
    MinecraftForge.EVENT_BUS.unregister(this);
  }

  @SubscribeEvent
  public void handleAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
    ICapabilityProvider capabilityProvider = event.getCapabilities().get(ILiving.ID);
    if (capabilityProvider != null) {
      capabilityProvider
          .getCapability(ModCapabilities.LIVING)
          .filter(living -> living instanceof Player)
          .map(living -> (Player<?>) living)
          .ifPresent(
              player -> player.registerExtension(SurvivalPlayer.ID, new SurvivalPlayer(player)));
    }
  }
}
