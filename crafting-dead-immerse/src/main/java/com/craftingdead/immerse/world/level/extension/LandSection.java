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

import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;

public class LandSection {

  public static final Codec<LandSection> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(LandClaim.CODEC
              .listOf()
              .fieldOf("landClaims")
              .forGetter(LandSection::getLandClaims))
          .apply(instance, LandSection::new));

  private final List<LandClaim> landClaims = new ObjectArrayList<>();

  public LandSection() {}

  public LandSection(List<LandClaim> landClaims) {
    this.landClaims.addAll(landClaims);
  }

  private List<LandClaim> getLandClaims() {
    return this.landClaims;
  }

  @Nullable
  public LandClaim getLandClaim(BlockPos blockPos) {
    for (var claim : this.landClaims) {
      if (claim.boundingBox().isInside(blockPos)) {
        return claim;
      }
    }
    return null;
  }

  public boolean registerLandClaim(LandClaim landClaim) {
    for (var otherRegion : this.landClaims) {
      if (otherRegion.boundingBox().intersects(landClaim.boundingBox())) {
        return false;
      }
    }
    this.landClaims.add(landClaim);
    return true;
  }

  public boolean removeLandClaim(LandClaim landClaim) {
    return this.landClaims.remove(landClaim);
  }
}
