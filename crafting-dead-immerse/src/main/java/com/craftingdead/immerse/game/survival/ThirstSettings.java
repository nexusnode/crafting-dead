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

package com.craftingdead.immerse.game.survival;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;

public record ThirstSettings(
    int decayIntervalTicks,
    int decayAmount,
    int sprintingDecayAmount,
    int damageIntervalTicks) {

  private static final int DECAY_INTERVAL_TICKS = 800;
  private static final int DECAY_AMOUNT = 5;
  private static final int SPRINTING_DECAY_AMOUNT = 10;
  private static final int DAMAGE_INTERVAL_TICKS = 120;

  public static final Codec<ThirstSettings> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Codec.INT
                  .optionalFieldOf("decay_interval_ticks", DECAY_INTERVAL_TICKS)
                  .forGetter(ThirstSettings::decayIntervalTicks),
              Codec.INT
                  .optionalFieldOf("decay_amount", DECAY_AMOUNT)
                  .forGetter(ThirstSettings::decayAmount),
              Codec.INT
                  .optionalFieldOf("sprinting_decay_amount", SPRINTING_DECAY_AMOUNT)
                  .forGetter(ThirstSettings::sprintingDecayAmount),
              Codec.INT
                  .optionalFieldOf("damage_interval_ticks", DAMAGE_INTERVAL_TICKS)
                  .forGetter(ThirstSettings::damageIntervalTicks))
          .apply(instance, ThirstSettings::new));

  public int decayAmountFor(Entity entity) {
    return entity.isSprinting() ? this.sprintingDecayAmount : this.decayAmount;
  }

  public static ThirstSettings createDefault() {
    return new ThirstSettings(
        DECAY_INTERVAL_TICKS,
        DECAY_AMOUNT,
        SPRINTING_DECAY_AMOUNT,
        DAMAGE_INTERVAL_TICKS);
  }
}
