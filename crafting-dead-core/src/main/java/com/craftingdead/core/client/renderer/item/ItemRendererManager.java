/**
 * Crafting Dead Copyright (C) 2020 Nexus Node
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
package com.craftingdead.core.client.renderer.item;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;

public class ItemRendererManager {

  private static final Logger logger = LogManager.getLogger();

  private final Map<IRegistryDelegate<Item>, IItemRenderer> itemRenderers =
      new Object2ObjectOpenHashMap<>();

  public void gatherItemRenderers() {
    StartupMessageManager.addModMessage("Gathering item renderers");
    logger.debug("Gathering item renderers");
    this.itemRenderers.clear();
    for (Item item : ForgeRegistries.ITEMS.getValues()) {
      if (item instanceof IRendererProvider) {
        this.itemRenderers.put(item.delegate, ((IRendererProvider) item).getRenderer());
      }
    }
  }

  public void refreshCachedModels() {
    StartupMessageManager.addModMessage("Refreshing cached models");
    this.itemRenderers.values().forEach(IItemRenderer::refreshCachedModels);
  }

  public Collection<ResourceLocation> getModelDependencies() {
    return this.itemRenderers.values().stream()
        .flatMap(itemRenderer -> itemRenderer.getModelDependencies().stream())
        .collect(Collectors.toSet());
  }

  public Collection<ResourceLocation> getTexturesToStitch() {
    return this.itemRenderers.values().stream()
        .flatMap(itemRenderer -> itemRenderer.getAdditionalModelTextures().stream())
        .collect(Collectors.toSet());
  }

  public Optional<IItemRenderer> getItemRenderer(Item item) {
    return Optional.ofNullable(this.itemRenderers.get(item.delegate));
  }
}
