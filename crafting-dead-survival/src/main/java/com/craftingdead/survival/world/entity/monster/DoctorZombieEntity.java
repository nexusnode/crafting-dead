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

package com.craftingdead.survival.world.entity.monster;

import org.jetbrains.annotations.Nullable;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.entity.ai.goal.ActionItemGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class DoctorZombieEntity extends Zombie {

  private static final int COOLDOWN_TICKS = 20 * 20;

  private int ticks;

  public DoctorZombieEntity(EntityType<? extends DoctorZombieEntity> type, Level world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(1, new InteractGoal(this, Player.class, 8.0F, 1.0F));
    this.goalSelector.addGoal(1,
        new ActionItemGoal(this, () -> this.ticks == 0, () -> this.ticks = COOLDOWN_TICKS));
  }

  @Override
  public void tick() {
    super.tick();
    if (this.ticks > 0) {
      this.ticks--;
    }
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
      MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
    groupData = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    this.getAttribute(Attributes.MAX_HEALTH)
        .setBaseValue(CraftingDeadSurvival.serverConfig.doctorZombieMaxHealth.get());
    this.getAttribute(Attributes.ATTACK_DAMAGE)
        .setBaseValue(CraftingDeadSurvival.serverConfig.doctorZombieAttackDamage.get());
    return groupData;
  }
}
