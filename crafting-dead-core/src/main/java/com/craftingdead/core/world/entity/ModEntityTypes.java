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

package com.craftingdead.core.world.entity;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.entity.grenade.C4ExplosiveEntity;
import com.craftingdead.core.world.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FragGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.PipeGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.SmokeGrenadeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.ENTITIES, CraftingDead.ID);

  public static final RegistryObject<EntityType<C4ExplosiveEntity>> C4_EXPLOSIVE =
      ENTITY_TYPES.register("c4_explosive", () -> create("c4_explosive",
          EntityType.Builder
              .<C4ExplosiveEntity>of(C4ExplosiveEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.65F, 0.25F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<DecoyGrenadeEntity>> DECOY_GRENADE =
      ENTITY_TYPES.register("decoy_grenade", () -> create("decoy_grenade",
          EntityType.Builder
              .<DecoyGrenadeEntity>of(DecoyGrenadeEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FireGrenadeEntity>> FIRE_GRENADE =
      ENTITY_TYPES.register("fire_grenade", () -> create("fire_grenade",
          EntityType.Builder
              .<FireGrenadeEntity>of(FireGrenadeEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FlashGrenadeEntity>> FLASH_GRENADE =
      ENTITY_TYPES.register("flash_grenade", () -> create("flash_grenade",
          EntityType.Builder
              .<FlashGrenadeEntity>of(FlashGrenadeEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FragGrenadeEntity>> FRAG_GRENADE =
      ENTITY_TYPES.register("frag_grenade", () -> create("frag_grenade",
          EntityType.Builder
              .<FragGrenadeEntity>of(FragGrenadeEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.25F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<PipeGrenadeEntity>> PIPE_GRENADE =
      ENTITY_TYPES.register("pipe_grenade", () -> create("pipe_grenade",
          EntityType.Builder
              .<PipeGrenadeEntity>of(PipeGrenadeEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<SmokeGrenadeEntity>> SMOKE_GRENADE =
      ENTITY_TYPES.register("smoke_grenade", () -> create("smoke_grenade",
          EntityType.Builder
              .<SmokeGrenadeEntity>of(SmokeGrenadeEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<SupplyDropEntity>> SUPPLY_DROP =
      ENTITY_TYPES.register("supply_drop", () -> create("supply_drop", EntityType.Builder
          .of(SupplyDropEntity::new, EntityClassification.MISC)));

  private static <T extends Entity> EntityType<T> create(String registryName,
      EntityType.Builder<T> builder) {
    return builder.build(new ResourceLocation(CraftingDead.ID, registryName).toString());
  }
}
