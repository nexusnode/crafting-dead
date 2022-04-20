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

import java.util.UUID;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.SerializableUUID;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public record LandClaim(BoundingBox boundingBox, UUID ownerId) {

  public static final Codec<LandClaim> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          BoundingBox.CODEC.fieldOf("boundingBox").forGetter(LandClaim::boundingBox),
          SerializableUUID.CODEC.fieldOf("ownerId").forGetter(LandClaim::ownerId))
      .apply(instance, LandClaim::new));
}
