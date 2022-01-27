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

package com.craftingdead.core.client.renderer.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;

public class ItemRendererManager {

  private static final String FILE_EXTENSION = ".json";
  private static final int PATH_SUFFIX_LENGTH = FILE_EXTENSION.length();
  private static final String DIRECTORY = "renderers/item";

  private static final Gson gson = new Gson();

  private static final Logger logger = LogManager.getLogger();

  private Map<IRegistryDelegate<Item>, CustomItemRenderer> renderers = Collections.emptyMap();

  private Map<ResourceLocation, Set<ResourceLocation>> materials = Collections.emptyMap();

  public void refreshCachedModels() {
    StartupMessageManager.addModMessage("Refreshing cached models");
    this.renderers.values().forEach(CustomItemRenderer::refreshCachedModels);
  }

  public Set<ResourceLocation> getTextures(ResourceLocation atlasLocation) {
    return this.materials.getOrDefault(atlasLocation, Collections.emptySet());
  }

  public CustomItemRenderer getItemRenderer(Item item) {
    return this.renderers.get(item.delegate);
  }

  public boolean renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType,
      @Nullable LivingEntity livingEntity, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    CustomItemRenderer renderer = this.renderers.get(itemStack.getItem().delegate);
    if (renderer != null && renderer.handlePerspective(itemStack, transformType)) {
      renderer.renderItem(itemStack, transformType, livingEntity, matrixStack, renderTypeBuffer,
          packedLight, packedOverlay);
      return true;
    }
    return false;
  }

  public Set<ResourceLocation> gatherItemRenderers(IResourceManager resourceManager,
      IProfiler profiler) {
    logger.debug("Gathering item renderers");
    profiler.startTick();

    Set<ResourceLocation> modelDependencies = new HashSet<>();
    this.renderers = new HashMap<>();

    int i = DIRECTORY.length() + 1;

    for (ResourceLocation location : resourceManager.listResources(DIRECTORY,
        file -> file.endsWith(FILE_EXTENSION))) {

      String path = location.getPath();
      ResourceLocation pathNoFileExtension = new ResourceLocation(location.getNamespace(),
          path.substring(i, path.length() - PATH_SUFFIX_LENGTH));

      Item item = ForgeRegistries.ITEMS.getValue(pathNoFileExtension);
      if (item == null) {
        logger.warn("No matching item for item renderer: {}", pathNoFileExtension.toString());
        continue;
      }

      try (
          IResource resource = resourceManager.getResource(location);
          InputStream inputStream = resource.getInputStream();
          Reader reader =
              new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        profiler.push(resource.getSourceName());
        profiler.push("parse");
        JsonElement json = JSONUtils.fromJson(gson, reader, JsonElement.class);
        if (json != null) {
          CustomItemRenderer renderer = CustomItemRenderer.CODEC
              .parse(JsonOps.INSTANCE, json)
              .getOrThrow(false, logger::error);
          CustomItemRenderer existing = this.renderers.put(item.delegate, renderer);
          if (existing != null) {
            throw new IllegalStateException(
                "Duplicate data file ignored with ID " + pathNoFileExtension);
          }
          modelDependencies.addAll(renderer.getModelDependencies(item));
        } else {
          logger.error("Couldn't load data file {} from {} as it's null or empty",
              pathNoFileExtension, location);
        }
        profiler.pop();
        profiler.pop();
      } catch (IllegalArgumentException | IOException | JsonParseException e) {
        logger.error("Couldn't parse data file {} from {}", pathNoFileExtension, location,
            e);
      }
    }

    profiler.push("materials");
    this.materials = this.renderers.values().stream()
        .map(CustomItemRenderer::getMaterials)
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(RenderMaterial::atlasLocation,
            Collectors.mapping(RenderMaterial::texture, Collectors.toSet())));
    profiler.pop();

    profiler.endTick();

    return modelDependencies;
  }

  protected ResourceLocation getPreparedPath(ResourceLocation rl) {
    return new ResourceLocation(rl.getNamespace(),
        DIRECTORY + File.separator + rl.getPath() + FILE_EXTENSION);
  }
}
