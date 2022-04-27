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

package com.craftingdead.core.world.item.gun;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GunTypeFactory extends ForgeRegistryEntry<GunTypeFactory> {

  public static final Codec<GunTypeFactory> CODEC =
      ResourceLocation.CODEC.flatXmap(registryName -> {
        var gunType = GunTypeFactories.registry.get().getValue(registryName);
        return gunType == null
            ? DataResult.error("Unknown registry key: " + registryName.toString())
            : DataResult.success(gunType);
      }, gameType -> DataResult.success(gameType.getRegistryName()));

  private final Codec<? extends GunType> gunTypeCodec;

  public GunTypeFactory(Codec<? extends GunType> gunTypeCodec) {
    this.gunTypeCodec = gunTypeCodec;
  }

  public Codec<? extends GunType> getGunTypeCodec() {
    return this.gunTypeCodec;
  }
}
