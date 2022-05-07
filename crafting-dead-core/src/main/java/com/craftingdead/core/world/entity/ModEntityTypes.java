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

  public static final DeferredRegister<EntityType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ENTITIES, CraftingDead.ID);

  public static final RegistryObject<EntityType<C4Explosive>> C4_EXPLOSIVE =
      deferredRegister.register("c4_explosive", () -> create("c4_explosive",
          EntityType.Builder
              .<C4Explosive>of(C4Explosive::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.65F, 0.25F)
              .setShouldReceiveVelocityUpdates(false)));
  public static final RegistryObject<EntityType<DecoyGrenadeEntity>> DECOY_GRENADE =
      deferredRegister.register("decoy_grenade", () -> create("decoy_grenade",
          EntityType.Builder
              .<DecoyGrenadeEntity>of(DecoyGrenadeEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FireGrenadeEntity>> FIRE_GRENADE =
      deferredRegister.register("fire_grenade", () -> create("fire_grenade",
          EntityType.Builder
              .<FireGrenadeEntity>of(FireGrenadeEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FlashGrenadeEntity>> FLASH_GRENADE =
      deferredRegister.register("flash_grenade", () -> create("flash_grenade",
          EntityType.Builder
              .<FlashGrenadeEntity>of(FlashGrenadeEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.5F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<FragGrenade>> FRAG_GRENADE =
      deferredRegister.register("frag_grenade", () -> create("frag_grenade",
          EntityType.Builder
              .<FragGrenade>of(FragGrenade::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(4)
              .sized(0.25F, 0.25F)
              .setShouldReceiveVelocityUpdates(false)));

  public static final RegistryObject<EntityType<SmokeGrenadeEntity>> SMOKE_GRENADE =
      deferredRegister.register("smoke_grenade", () -> create("smoke_grenade",
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
