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

package com.craftingdead.immerse.world.level.extension;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class LandOwnerType extends ForgeRegistryEntry<LandOwnerType> {

  public static final Codec<LandOwnerType> CODEC =
      ExtraCodecs.lazyInitializedCodec(() -> LandOwnerTypes.registry.get().getCodec());

  private final Codec<? extends LandOwner> codec;
  private final Codec<? extends LandOwner> networkCodec;

  public LandOwnerType(Codec<? extends LandOwner> codec, Codec<? extends LandOwner> networkCodec) {
    this.codec = codec;
    this.networkCodec = networkCodec;
  }

  public Codec<? extends LandOwner> getCodec() {
    return this.codec;
  }

  public Codec<? extends LandOwner> getNetworkCodec() {
    return this.networkCodec;
  }
}
