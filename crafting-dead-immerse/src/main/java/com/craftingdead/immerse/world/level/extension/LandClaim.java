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
