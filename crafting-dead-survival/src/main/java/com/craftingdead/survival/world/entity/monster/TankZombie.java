/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.world.entity.monster;

import java.util.Objects;
import javax.annotation.Nullable;
import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class TankZombie extends AdvancedZombie {

  public TankZombie(EntityType<? extends TankZombie> type, Level world) {
    super(type, world);
  }

  public static AttributeSupplier.Builder registerAttributes() {
    return AdvancedZombie.attributeTemplate()
        .add(Attributes.FOLLOW_RANGE, 20.0D)
        .add(Attributes.MAX_HEALTH, 100.0D)
        .add(Attributes.ATTACK_DAMAGE, 15.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.19D)
        .add(Attributes.ATTACK_SPEED, 2.0D);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
      MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
    groupData = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    if (!level.isClientSide()) {
      Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH))
          .setBaseValue(CraftingDeadSurvival.serverConfig.tankZombieMaxHealth.get());
      Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE))
          .setBaseValue(CraftingDeadSurvival.serverConfig.tankZombieAttackDamage.get());
    }
    return groupData;
  }
}
