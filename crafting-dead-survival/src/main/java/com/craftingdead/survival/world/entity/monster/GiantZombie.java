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

import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.ammoprovider.RefillableAmmoProvider;
import com.craftingdead.survival.CraftingDeadSurvival;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class GiantZombie extends AdvancedZombie {

  public GiantZombie(EntityType<? extends AdvancedZombie> type, Level world) {
    super(type, world);
  }

  @Override
  protected ItemStack createHeldItem() {
    ItemStack gunStack = ModItems.M4A1.get().getDefaultInstance();
    gunStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> gun.setAmmoProvider(
        new RefillableAmmoProvider(ModItems.RPK_MAGAZINE.get().getDefaultInstance(), 0, true)));
    return gunStack;
  }

  @Override
  protected ItemStack createClothingItem() {
    return ModItems.ARMY_CLOTHING.get().getDefaultInstance();
  }

  @Override
  protected ItemStack getHatStack() {
    return ModItems.ARMY_HELMET.get().getDefaultInstance();
  }

  public static AttributeSupplier.Builder createAttributes() {
    return AdvancedZombie.attributeTemplate()
        .add(Attributes.MAX_HEALTH, 100.0D)
        .add(Attributes.ATTACK_DAMAGE, 50.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.5D);
  }

  @Override
  protected float getStandingEyeHeight(Pose pose, EntityDimensions entitySize) {
    return 10.440001F;
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
      MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
    groupData = super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    if (!level.isClientSide()) {
      Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH))
          .setBaseValue(CraftingDeadSurvival.serverConfig.giantZombieMaxHealth.get());
      Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE))
          .setBaseValue(CraftingDeadSurvival.serverConfig.giantZombieAttackDamage.get());
    }
    return groupData;
  }
}
