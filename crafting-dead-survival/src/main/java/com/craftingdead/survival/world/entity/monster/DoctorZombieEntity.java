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
import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.entity.ai.goal.ActionItemGoal;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class DoctorZombieEntity extends AdvancedZombie {

  private static final int COOLDOWN_TICKS = 20 * 20;

  private int ticks;

  public DoctorZombieEntity(EntityType<? extends AdvancedZombie> type, Level world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(1, new InteractGoal(this, Player.class, 8.0F, 1.0F));
    this.goalSelector.addGoal(1,
        new ActionItemGoal(this, () -> this.ticks == 0, () -> this.ticks = COOLDOWN_TICKS));
  }

  @SuppressWarnings("deprecation")
  @Override
  protected ItemStack createHeldItem() {
    return Registry.ITEM.getTag(ModItemTags.SYRINGES)
        .flatMap(set -> set.getRandomElement(this.random))
        .map(Holder::value)
        .map(Item::getDefaultInstance)
        .orElse(ItemStack.EMPTY);
  }

  @Override
  protected ItemStack createClothingItem() {
    return ModItems.DOCTOR_CLOTHING.get().getDefaultInstance();
  }

  @Override
  protected ItemStack getHatStack() {
    return ModItems.DOCTOR_MASK.get().getDefaultInstance();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.ticks > 0) {
      this.ticks--;
    }
  }

  public static AttributeSupplier.Builder createAttributes() {
    return AdvancedZombie.attributeTemplate()
        .add(Attributes.MAX_HEALTH, 20.0D)
        .add(Attributes.ATTACK_DAMAGE, 3.0D);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
      MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
    groupData = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    if (!level.isClientSide()) {
      Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH))
          .setBaseValue(CraftingDeadSurvival.serverConfig.doctorZombieMaxHealth.get());
      Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE))
          .setBaseValue(CraftingDeadSurvival.serverConfig.doctorZombieAttackDamage.get());
    }
    return groupData;
  }
}
