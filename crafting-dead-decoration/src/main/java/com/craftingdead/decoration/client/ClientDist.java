package com.craftingdead.decoration.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientDist {

  public ClientDist() {
    var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleClientSetup);
  }

  private void handleClientSetup(FMLClientSetupEvent event) {
    RenderLayers.register();
  }
}
