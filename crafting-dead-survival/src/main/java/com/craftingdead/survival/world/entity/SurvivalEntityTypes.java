/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.survival.world.entity;

import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.entity.grenade.PipeGrenadeEntity;
import com.craftingdead.survival.world.entity.monster.AdvancedZombieEntity;
import com.craftingdead.survival.world.entity.monster.DoctorZombieEntity;
import com.craftingdead.survival.world.entity.monster.FastZombieEntity;
import com.craftingdead.survival.world.entity.monster.GiantZombieEntity;
import com.craftingdead.survival.world.entity.monster.PoliceZombieEntity;
import com.craftingdead.survival.world.entity.monster.TankZombieEntity;
import com.craftingdead.survival.world.entity.monster.WeakZombieEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class SurvivalEntityTypes {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.ENTITIES, CraftingDeadSurvival.ID);

  public static final RegistryObject<EntityType<PipeGrenadeEntity>> PIPE_GRENADE =
      ENTITY_TYPES.register("pipe_grenade", () -> create("pipe_grenade",
          EntityType.Builder
              .<PipeGrenadeEntity>of(PipeGrenadeEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<SupplyDropEntity>> SUPPLY_DROP =
      ENTITY_TYPES.register("supply_drop",
          () -> create("supply_drop",
              EntityType.Builder.of(SupplyDropEntity::new, EntityClassification.MISC)));

  public static final RegistryObject<EntityType<AdvancedZombieEntity>> ADVANCED_ZOMBIE =
      ENTITY_TYPES.register("advanced_zombie", () -> create("advanced_zombie",
          EntityType.Builder
              .<AdvancedZombieEntity>of(AdvancedZombieEntity::new, EntityClassification.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<FastZombieEntity>> FAST_ZOMBIE =
      ENTITY_TYPES.register("fast_zombie", () -> create("fast_zombie",
          EntityType.Builder
              .<FastZombieEntity>of(FastZombieEntity::new, EntityClassification.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<TankZombieEntity>> TANK_ZOMBIE =
      ENTITY_TYPES.register("tank_zombie", () -> create("tank_zombie",
          EntityType.Builder
              .<TankZombieEntity>of(TankZombieEntity::new, EntityClassification.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<WeakZombieEntity>> WEAK_ZOMBIE =
      ENTITY_TYPES.register("weak_zombie", () -> create("weak_zombie",
          EntityType.Builder
              .<WeakZombieEntity>of(WeakZombieEntity::new, EntityClassification.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<AdvancedZombieEntity>> POLICE_ZOMBIE =
      ENTITY_TYPES.register("police_zombie", () -> create("police_zombie",
          EntityType.Builder
              .<AdvancedZombieEntity>of(PoliceZombieEntity::new, EntityClassification.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<AdvancedZombieEntity>> DOCTOR_ZOMBIE =
      ENTITY_TYPES.register("doctor_zombie", () -> create("doctor_zombie",
          EntityType.Builder
              .<AdvancedZombieEntity>of(DoctorZombieEntity::new, EntityClassification.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<GiantZombieEntity>> GIANT_ZOMBIE =
      ENTITY_TYPES.register("giant_zombie", () -> create("giant_zombie",
          EntityType.Builder
              .<GiantZombieEntity>of(GiantZombieEntity::new,
                  EntityClassification.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(3.6F, 12.0F)
              .setShouldReceiveVelocityUpdates(false)));

  private static <T extends Entity> EntityType<T> create(String registryName,
      EntityType.Builder<T> builder) {
    return builder.build(new ResourceLocation(CraftingDeadSurvival.ID, registryName).toString());
  }
}
