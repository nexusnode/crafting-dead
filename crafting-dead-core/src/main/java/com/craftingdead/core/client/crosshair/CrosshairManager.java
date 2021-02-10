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

import com.craftingdead.core.CraftingDead;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrosshairManager extends ReloadListener<Map<ResourceLocation, Crosshair>> {

  public static final ResourceLocation DEFAULT_CROSSHAIR =
      new ResourceLocation(CraftingDead.ID, "standard");

  private static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();

  private static final Logger LOGGER = LogManager.getLogger();

  private final Map<ResourceLocation, Crosshair> loadedCrosshairs = Maps.newHashMap();

  public Crosshair getCrosshair(ResourceLocation crosshairLocation) {
    Crosshair crosshair = this.loadedCrosshairs.get(crosshairLocation);
    return crosshair != null ? crosshair : this.loadedCrosshairs.get(DEFAULT_CROSSHAIR);
  }

  @Override
  protected Map<ResourceLocation, Crosshair> prepare(IResourceManager resourceManager,
      IProfiler profiler) {
    Map<ResourceLocation, Crosshair> allCrosshairs = Maps.newHashMap();
    for (String domain : resourceManager.getResourceNamespaces()) {
      try {
        resourceManager.getAllResources(new ResourceLocation(domain, "crosshairs.json"))
            .forEach((resource) -> {
              try (InputStream input = resource.getInputStream()) {
                Crosshair[] crosshairs = JSONUtils.fromJson(GSON,
                    new InputStreamReader(input, StandardCharsets.UTF_8), Crosshair[].class);
                for (Crosshair crosshair : crosshairs) {
                  allCrosshairs.put(crosshair.getName(), crosshair);
                }
              } catch (IOException e) {
                LOGGER.warn("Invalid crosshairs.json in resourcepack: '{}'", resource.getPackName(),
                    e);
              }
            });
      } catch (IOException e) {
        ;
      }
    }
    return allCrosshairs;
  }

  @Override
  protected void apply(Map<ResourceLocation, Crosshair> crosshairs,
      IResourceManager resourceManager, IProfiler profiler) {
    this.loadedCrosshairs.clear();
    this.loadedCrosshairs.putAll(crosshairs);
  }
}
