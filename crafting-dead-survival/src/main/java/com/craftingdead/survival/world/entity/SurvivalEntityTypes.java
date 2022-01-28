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
import com.craftingdead.survival.world.entity.grenade.PipeGrenade;
import com.craftingdead.survival.world.entity.monster.AdvancedZombie;
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

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.ENTITIES, CraftingDeadSurvival.ID);

  public static final RegistryObject<EntityType<PipeGrenade>> PIPE_GRENADE =
      ENTITY_TYPES.register("pipe_grenade", () -> create("pipe_grenade",
          EntityType.Builder
              .<PipeGrenade>of(PipeGrenade::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<SupplyDrop>> SUPPLY_DROP =
      ENTITY_TYPES.register("supply_drop",
          () -> create("supply_drop",
              EntityType.Builder.of(SupplyDrop::new, MobCategory.MISC)));

  public static final RegistryObject<EntityType<AdvancedZombie>> ADVANCED_ZOMBIE =
      ENTITY_TYPES.register("advanced_zombie", () -> create("advanced_zombie",
          EntityType.Builder
              .<AdvancedZombie>of(AdvancedZombie::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<FastZombie>> FAST_ZOMBIE =
      ENTITY_TYPES.register("fast_zombie", () -> create("fast_zombie",
          EntityType.Builder
              .<FastZombie>of(FastZombie::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<TankZombie>> TANK_ZOMBIE =
      ENTITY_TYPES.register("tank_zombie", () -> create("tank_zombie",
          EntityType.Builder
              .<TankZombie>of(TankZombie::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<WeakZombie>> WEAK_ZOMBIE =
      ENTITY_TYPES.register("weak_zombie", () -> create("weak_zombie",
          EntityType.Builder
              .<WeakZombie>of(WeakZombie::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<AdvancedZombie>> POLICE_ZOMBIE =
      ENTITY_TYPES.register("police_zombie", () -> create("police_zombie",
          EntityType.Builder
              .<AdvancedZombie>of(PoliceZombieEntity::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<AdvancedZombie>> DOCTOR_ZOMBIE =
      ENTITY_TYPES.register("doctor_zombie", () -> create("doctor_zombie",
          EntityType.Builder
              .<AdvancedZombie>of(DoctorZombieEntity::new, MobCategory.MONSTER)
              .setTrackingRange(64)
              .setUpdateInterval(3)
              .sized(0.6F, 1.95F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<GiantZombie>> GIANT_ZOMBIE =
      ENTITY_TYPES.register("giant_zombie", () -> create("giant_zombie",
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
