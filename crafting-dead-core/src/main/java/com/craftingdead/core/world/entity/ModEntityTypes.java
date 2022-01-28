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
import com.craftingdead.core.world.entity.grenade.C4Explosive;
import com.craftingdead.core.world.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.world.entity.grenade.FragGrenade;
import com.craftingdead.core.world.entity.grenade.SmokeGrenadeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.ENTITIES, CraftingDead.ID);

  public static final RegistryObject<EntityType<C4Explosive>> C4_EXPLOSIVE =
      ENTITY_TYPES.register("c4_explosive", () -> create("c4_explosive",
          EntityType.Builder
              .<C4Explosive>of(C4Explosive::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.65F, 0.25F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<DecoyGrenadeEntity>> DECOY_GRENADE =
      ENTITY_TYPES.register("decoy_grenade", () -> create("decoy_grenade",
          EntityType.Builder
              .<DecoyGrenadeEntity>of(DecoyGrenadeEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FireGrenadeEntity>> FIRE_GRENADE =
      ENTITY_TYPES.register("fire_grenade", () -> create("fire_grenade",
          EntityType.Builder
              .<FireGrenadeEntity>of(FireGrenadeEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FlashGrenadeEntity>> FLASH_GRENADE =
      ENTITY_TYPES.register("flash_grenade", () -> create("flash_grenade",
          EntityType.Builder
              .<FlashGrenadeEntity>of(FlashGrenadeEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FragGrenade>> FRAG_GRENADE =
      ENTITY_TYPES.register("frag_grenade", () -> create("frag_grenade",
          EntityType.Builder
              .<FragGrenade>of(FragGrenade::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.25F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<SmokeGrenadeEntity>> SMOKE_GRENADE =
      ENTITY_TYPES.register("smoke_grenade", () -> create("smoke_grenade",
          EntityType.Builder
              .<SmokeGrenadeEntity>of(SmokeGrenadeEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));


  private static <T extends Entity> EntityType<T> create(String registryName,
      EntityType.Builder<T> builder) {
    return builder.build(new ResourceLocation(CraftingDead.ID, registryName).toString());
  }
}
