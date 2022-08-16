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
import net.minecraft.SharedConstants;

/**
 * @param enabled - if thirst is enabled or not
 * @param thirstDamageDelayTicks - delay in ticks before dealing thirst damage
 * @param idleDrainDelayTicks - delay in ticks between natural thirst decay
 * @param idleDrain - how much hydration is drained from the player over natural dehydration
 * @param sprintDrain - how much hydration is drained from the player over sprinting ticks
 * @param attackDrain - how much hydration is drained from the player when attacking
 * @param miningDrain - how much hydration is drained from the player when mining
 */
public record ThirstProperties(boolean enabled, int thirstDamageDelayTicks, int idleDrainDelayTicks,
    float idleDrain, float sprintDrain, float attackDrain, float miningDrain) {

  public static ThirstProperties DEFAULT =
      new ThirstProperties(false, SharedConstants.TICKS_PER_SECOND * 6,
          SharedConstants.TICKS_PER_SECOND * 6, 0.01F, 0.01F, 0.01F, 0.01F);

  public static final Codec<ThirstProperties> CODEC =
      RecordCodecBuilder.create(
          instance -> instance
              .group(
                  Codec.BOOL.optionalFieldOf("enabled", false).forGetter(ThirstProperties::enabled),
                  Codec.INT
                      .optionalFieldOf("thirst_damage_delay_ticks",
                          DEFAULT.thirstDamageDelayTicks())
                      .forGetter(ThirstProperties::thirstDamageDelayTicks),
                  Codec.INT.optionalFieldOf("idle_drain_delay_ticks", DEFAULT.idleDrainDelayTicks())
                      .forGetter(ThirstProperties::idleDrainDelayTicks),
                  Codec.FLOAT.optionalFieldOf("idle_drain", DEFAULT.idleDrain())
                      .forGetter(ThirstProperties::idleDrain),
                  Codec.FLOAT.optionalFieldOf("sprint_drain", DEFAULT.sprintDrain())
                      .forGetter(ThirstProperties::sprintDrain),
                  Codec.FLOAT.optionalFieldOf("attack_drain", DEFAULT.attackDrain())
                      .forGetter(ThirstProperties::attackDrain),
                  Codec.FLOAT.optionalFieldOf("mining_drain", DEFAULT.miningDrain())
                      .forGetter(ThirstProperties::miningDrain))
              .apply(instance, ThirstProperties::new));
}
