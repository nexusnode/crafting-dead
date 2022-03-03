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

package com.craftingdead.core.client.crosshair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import com.craftingdead.core.CraftingDead;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;

public class CrosshairManager extends SimplePreparableReloadListener<Map<ResourceLocation, Crosshair>> {

  public static final ResourceLocation DEFAULT_CROSSHAIR =
      new ResourceLocation(CraftingDead.ID, "standard");

  private static final Gson gson = new Gson();

  private static final Logger logger = LogUtils.getLogger();

  private final Map<ResourceLocation, Crosshair> loadedCrosshairs = new HashMap<>();

  public Crosshair getCrosshair(ResourceLocation crosshairLocation) {
    Crosshair crosshair = this.loadedCrosshairs.get(crosshairLocation);
    return crosshair != null ? crosshair : this.loadedCrosshairs.get(DEFAULT_CROSSHAIR);
  }

  @Override
  protected Map<ResourceLocation, Crosshair> prepare(ResourceManager resourceManager,
      ProfilerFiller profiler) {
    ImmutableMap.Builder<ResourceLocation, Crosshair> crosshairs = ImmutableMap.builder();
    for (String domain : resourceManager.getNamespaces()) {
      ResourceLocation fileLocation = new ResourceLocation(domain, "crosshairs.json");
      try {
        resourceManager.getResources(fileLocation)
            .forEach((resource) -> {
              try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                Codec.list(Crosshair.CODEC)
                    .parse(JsonOps.INSTANCE, gson.fromJson(reader, JsonElement.class))
                    .getOrThrow(false, message -> logger.warn("Failed to parse {} Reason: ",
                        fileLocation.toString(), message))
                    .forEach(crosshair -> crosshairs.put(crosshair.getName(), crosshair));
              } catch (IOException e) {
                logger.warn("Failed to read {}", fileLocation.toString(), e);
              }
            });
      } catch (IOException e) {
        ;
      }
    }
    return crosshairs.build();
  }

  @Override
  protected void apply(Map<ResourceLocation, Crosshair> crosshairs,
      ResourceManager resourceManager, ProfilerFiller profiler) {
    this.loadedCrosshairs.clear();
    this.loadedCrosshairs.putAll(crosshairs);
  }
}
