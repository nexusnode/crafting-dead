/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.renderer.item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IRegistryDelegate;

public class ItemRenderDispatcher implements ResourceManagerReloadListener {

  private static final String FILE_EXTENSION = ".json";
  private static final int PATH_SUFFIX_LENGTH = FILE_EXTENSION.length();
  private static final String DIRECTORY = "renderers/item";

  private static final Gson gson = new Gson();

  private static final Logger logger = LogUtils.getLogger();

  private final Minecraft minecraft = Minecraft.getInstance();

  private Map<IRegistryDelegate<Item>, CustomItemRenderer> renderers = Map.of();

  private Map<ResourceLocation, Set<ResourceLocation>> materials = Map.of();

  public Set<ResourceLocation> getTextures(ResourceLocation atlasLocation) {
    return this.materials.getOrDefault(atlasLocation, Collections.emptySet());
  }

  public CustomItemRenderer getItemRenderer(Item item) {
    return this.renderers.get(item.delegate);
  }

  public boolean renderItem(ItemStack itemStack, ItemTransforms.TransformType transformType,
      @Nullable LivingExtension<?, ?> living, PoseStack poseStack, MultiBufferSource bufferSource,
      int packedLight, int packedOverlay) {
    var renderer = this.renderers.get(itemStack.getItem().delegate);
    if (renderer != null && renderer.handlePerspective(itemStack, transformType)) {
      renderer.renderItem(itemStack, transformType, living, poseStack, bufferSource,
          packedLight, packedOverlay);
      return true;
    }
    return false;
  }

  public Set<ResourceLocation> gatherItemRenderers(ResourceManager resourceManager,
      ProfilerFiller profiler) {
    logger.debug("Gathering item renderers");
    profiler.startTick();

    Set<ResourceLocation> modelDependencies = new HashSet<>();
    var renderers = ImmutableMap.<IRegistryDelegate<Item>, CustomItemRenderer>builder();

    int i = DIRECTORY.length() + 1;

    for (var location : resourceManager.listResources(DIRECTORY,
        file -> file.endsWith(FILE_EXTENSION))) {

      var path = location.getPath();
      var pathNoFileExtension = new ResourceLocation(location.getNamespace(),
          path.substring(i, path.length() - PATH_SUFFIX_LENGTH));

      var item = ForgeRegistries.ITEMS.getValue(pathNoFileExtension);
      if (item == null) {
        logger.warn("No matching item for item renderer: {}", pathNoFileExtension.toString());
        continue;
      }

      try (
          var resource = resourceManager.getResource(location);
          var inputStream = resource.getInputStream();
          var reader =
              new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        profiler.push(resource.getSourceName());
        profiler.push("parse");
        var json = GsonHelper.fromJson(gson, reader, JsonElement.class);
        if (json != null) {
          var renderer = tryCreate(item, new Dynamic<>(JsonOps.INSTANCE, json));
          if (renderer != null) {
            renderers.put(item.delegate, renderer);
            modelDependencies.addAll(renderer.getModelDependencies());
          }
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

    this.renderers = renderers.build();

    profiler.push("materials");
    this.materials = this.renderers.values().stream()
        .map(CustomItemRenderer::getMaterials)
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(Material::atlasLocation,
            Collectors.mapping(Material::texture, Collectors.toSet())));
    profiler.pop();

    profiler.endTick();

    return modelDependencies;
  }

  private static <I extends Item, P extends ItemRendererProperties, T extends ItemRendererType<I, P>> CustomItemRenderer tryCreate(
      Item item, Dynamic<?> dynamic) {
    @SuppressWarnings("unchecked")
    var properties =
        (P) ItemRendererProperties.CODEC.parse(dynamic).getOrThrow(false, logger::error);

    @SuppressWarnings("unchecked")
    var type = (ItemRendererType<I, P>) properties.getItemRendererType();

    if (type.getItemType().isInstance(item)) {
      return type.create(type.getItemType().cast(item), properties);
    } else {
      logger.error("Item renderer {} expects item of type: {}",
          ItemRendererTypes.getKey(type).get(), type.getItemType().getName());
      return null;
    }
  }

  @Override
  public void onResourceManagerReload(ResourceManager resourceManager) {
    StartupMessageManager.addModMessage("Refreshing cached models");
    this.renderers.values()
        .forEach(
            renderer -> renderer.refreshCachedModels(this.minecraft.getEntityModels()::bakeLayer));
  }
}
