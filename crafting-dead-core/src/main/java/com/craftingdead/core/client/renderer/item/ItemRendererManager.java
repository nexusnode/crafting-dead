package com.craftingdead.core.client.renderer.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRendererManager {

  private static final Logger logger = LogManager.getLogger();

  private final List<IItemRenderer> itemRenderers = new ArrayList<>();

  public void gatherItemRenderers() {
    StartupMessageManager.addModMessage("Gathering item renderers");
    logger.debug("Gathering item renderers");
    this.itemRenderers.clear();
    for (Item item : ForgeRegistries.ITEMS.getValues()) {
      if (item instanceof IRendererProvider) {
        this.itemRenderers.add(((IRendererProvider) item).getRenderer());
      }
    }
  }

  public void refreshCachedModels() {
    StartupMessageManager.addModMessage("Refreshing cached models");
    this.itemRenderers.forEach(IItemRenderer::refreshCachedModels);
  }

  public Collection<ResourceLocation> getModelDependencies() {
    return this.itemRenderers.stream()
        .flatMap(itemRenderer -> itemRenderer.getModelDependencies().stream())
        .collect(Collectors.toSet());
  }

  public Collection<ResourceLocation> getTexturesToStitch() {
    return this.itemRenderers.stream()
        .flatMap(itemRenderer -> itemRenderer.getAdditionalModelTextures().stream())
        .collect(Collectors.toSet());
  }
}
