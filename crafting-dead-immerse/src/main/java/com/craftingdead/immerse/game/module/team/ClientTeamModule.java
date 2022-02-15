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

package com.craftingdead.immerse.game.module.team;

import java.util.function.Supplier;
import com.craftingdead.core.event.RenderArmClothingEvent;
import com.craftingdead.immerse.client.ClientDist;
import com.craftingdead.immerse.game.module.GameModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientTeamModule<T extends Enum<T> & Team> extends TeamModule<T>
    implements GameModule.Tickable {

  private final Minecraft minecraft = Minecraft.getInstance();

  private final Supplier<? extends Screen> switchTeamsScreen;

  public ClientTeamModule(Class<T> teamType, Supplier<? extends Screen> switchTeamsScreen) {
    super(teamType);
    this.switchTeamsScreen = switchTeamsScreen;
  }

  @Override
  public void tick() {
    while (ClientDist.SWITCH_TEAMS.consumeClick()) {
      this.minecraft.setScreen(this.switchTeamsScreen.get());
    }
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
  public void handleRenderArmClothing(RenderArmClothingEvent event) {
    // We need to have team as a variable because the compiler doesn't like it if we use flatMap
    // on Team.getSkin because of something to do with generics.
    Team team = this.getPlayerTeam(event.getPlayerEntity().getUUID()).orElse(null);
    if (team != null) {
      team.getSkin().ifPresent(event::setClothingTexture);
    }
  }
}
