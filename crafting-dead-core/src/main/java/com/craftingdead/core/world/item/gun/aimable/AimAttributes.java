package com.craftingdead.core.world.item.gun.aimable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * @param boltAction - Whether the gun has bolt action
 */
public record AimAttributes(boolean boltAction) {

  public static final Codec<AimAttributes> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(Codec.BOOL
              .optionalFieldOf("bolt_action", false)
              .forGetter(AimAttributes::boltAction))
          .apply(instance, AimAttributes::new));
}
