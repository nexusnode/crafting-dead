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
