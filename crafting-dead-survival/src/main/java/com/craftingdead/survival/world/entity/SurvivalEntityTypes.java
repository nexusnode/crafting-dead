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

package com.craftingdead.survival.world.entity;

import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.entity.grenade.PipeBomb;
import com.craftingdead.survival.world.entity.monster.DoctorZombieEntity;
import com.craftingdead.survival.world.entity.monster.FastZombie;
import com.craftingdead.survival.world.entity.monster.GiantZombie;
import com.craftingdead.survival.world.entity.monster.PoliceZombieEntity;
import com.craftingdead.survival.world.entity.monster.TankZombie;
import com.craftingdead.survival.world.entity.monster.WeakZombie;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class SurvivalEntityTypes {

  public static final DeferredRegister<EntityType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ENTITIES, CraftingDeadSurvival.ID);

  public static final RegistryObject<EntityType<PipeBomb>> PIPE_BOMB =
      deferredRegister.register("pipe_bomb", () -> create("pipe_bomb",
          EntityType.Builder
              .<PipeBomb>of(PipeBomb::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<SupplyDrop>> SUPPLY_DROP =
      deferredRegister.register("supply_drop",
          () -> create("supply_drop",
              EntityType.Builder.of(SupplyDrop::new, MobCategory.MISC)));

  public static final RegistryObject<EntityType<FastZombie>> FAST_ZOMBIE =
      deferredRegister.register("fast_zombie", () -> create("fast_zombie",
          EntityType.Builder
              .<FastZombie>of(FastZombie::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<TankZombie>> TANK_ZOMBIE =
      deferredRegister.register("tank_zombie", () -> create("tank_zombie",
          EntityType.Builder
              .<TankZombie>of(TankZombie::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<WeakZombie>> WEAK_ZOMBIE =
      deferredRegister.register("weak_zombie", () -> create("weak_zombie",
          EntityType.Builder
              .<WeakZombie>of(WeakZombie::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<PoliceZombieEntity>> POLICE_ZOMBIE =
      deferredRegister.register("police_zombie", () -> create("police_zombie",
          EntityType.Builder
              .<PoliceZombieEntity>of(PoliceZombieEntity::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<DoctorZombieEntity>> DOCTOR_ZOMBIE =
      deferredRegister.register("doctor_zombie", () -> create("doctor_zombie",
          EntityType.Builder
              .<DoctorZombieEntity>of(DoctorZombieEntity::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<GiantZombie>> GIANT_ZOMBIE =
      deferredRegister.register("giant_zombie", () -> create("giant_zombie",
          EntityType.Builder
              .<GiantZombie>of(GiantZombie::new,
                  MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(3.6F, 12.0F)
              .setShouldReceiveVelocityUpdates(false)));

  private static <T extends Entity> EntityType<T> create(String registryName,
      EntityType.Builder<T> builder) {
    return builder.build(new ResourceLocation(CraftingDeadSurvival.ID, registryName).toString());
  }
}
