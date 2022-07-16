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

package com.craftingdead.core.client.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

// TODO: Too distant from where this record is effectively loaded?
//  Keeping it as inner class in the GunRendererProperties makes it quite ugly to reference it - juanmuscaria
public record AnimationProperties(int shootAnimationLength, float shootAnimationBounce,
    float shootAnimationKickback) {
  public static Codec<AnimationProperties> CODEC = RecordCodecBuilder.create(instance -> instance
      .group(
          Codec.INT.optionalFieldOf("shoot_animation_length", 5)
              .forGetter(AnimationProperties::shootAnimationLength),
          Codec.FLOAT.optionalFieldOf("shoot_animation_bounce", 2.0F)
              .forGetter(AnimationProperties::shootAnimationBounce),
          Codec.FLOAT.optionalFieldOf("shoot_animation_kickback", 0.25F)
              .forGetter(AnimationProperties::shootAnimationKickback))
      .apply(instance, AnimationProperties::new));

}
