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

package com.craftingdead.virus.client;

import com.craftingdead.virus.IModDist;
import com.craftingdead.virus.client.renderer.entity.AdvancedZombieRenderer;
import com.craftingdead.virus.client.renderer.entity.GiantZombieRenderer;
import com.craftingdead.virus.entity.ModEntityTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist implements IModDist {

  public ClientDist() {
    final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
    modBus.addListener(this::handleClientSetup);
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.advancedZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.fastZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.tankZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.weakZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.policeZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.doctorZombie,
        AdvancedZombieRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.giantZombie,
        GiantZombieRenderer::new);
  }
}
