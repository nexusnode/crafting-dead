/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
