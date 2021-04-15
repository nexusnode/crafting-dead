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

package com.craftingdead.core.client.crosshair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.CraftingDead;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class CrosshairManager extends ReloadListener<Map<ResourceLocation, Crosshair>> {

  public static final ResourceLocation DEFAULT_CROSSHAIR =
      new ResourceLocation(CraftingDead.ID, "standard");

  private static final Gson gson = new Gson();

  private static final Logger logger = LogManager.getLogger();

  private final Map<ResourceLocation, Crosshair> loadedCrosshairs = new HashMap<>();

  public Crosshair getCrosshair(ResourceLocation crosshairLocation) {
    Crosshair crosshair = this.loadedCrosshairs.get(crosshairLocation);
    return crosshair != null ? crosshair : this.loadedCrosshairs.get(DEFAULT_CROSSHAIR);
  }

  @Override
  protected Map<ResourceLocation, Crosshair> prepare(IResourceManager resourceManager,
      IProfiler profiler) {
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
      IResourceManager resourceManager, IProfiler profiler) {
    this.loadedCrosshairs.clear();
    this.loadedCrosshairs.putAll(crosshairs);
  }
}
