/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.data.guns;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import org.slf4j.Logger;
import com.craftingdead.core.world.item.gun.GunConfiguration;
import com.craftingdead.core.world.item.gun.GunConfigurations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public class GunDataProvider implements DataProvider {

  private static final Logger logger = LogUtils.getLogger();

  private final RegistryOps<JsonElement> ops =
      RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.BUILTIN.get());
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final DataGenerator dataGenerator;

  public GunDataProvider(DataGenerator dataGenerator) {
    this.dataGenerator = dataGenerator;
  }

  @Override
  public void run(HashCache cache) throws IOException {
    for (GunConfiguration gunType : GunConfigurations.registry.get()) {
      encodeGun(gunType, GunConfigurations.REGISTRY_KEY.location(), cache);
    }
  }

  private void encodeGun(GunConfiguration gun, ResourceLocation registryLocation, HashCache cache) {
    Path outputFolder = dataGenerator.getOutputFolder();
    var gunId = Objects.requireNonNull(gun.getRegistryName());
    final String pathString = String.join("/", PackType.SERVER_DATA.getDirectory(),
        gunId.getNamespace(), gunId.getNamespace(), registryLocation.getPath(),
        gunId.getPath() + ".json");
    final Path path = outputFolder.resolve(pathString);
    GunConfiguration.DIRECT_CODEC.encodeStart(ops, gun)
        .resultOrPartial(msg -> logger.error("Failed to encode {}: {}", path, msg))
        .ifPresent(json -> {
          try {
            DataProvider.save(gson, cache, json, path);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }

  @Override
  public String getName() {
    return "Crafting Dead Guns";
  }
}
