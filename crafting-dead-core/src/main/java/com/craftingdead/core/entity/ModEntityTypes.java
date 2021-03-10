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

package com.craftingdead.core.entity;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.entity.grenade.C4ExplosiveEntity;
import com.craftingdead.core.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.entity.grenade.FragGrenadeEntity;
import com.craftingdead.core.entity.grenade.PipeGrenadeEntity;
import com.craftingdead.core.entity.grenade.SmokeGrenadeEntity;
import com.craftingdead.core.entity.monster.AdvancedZombieEntity;
import com.craftingdead.core.entity.monster.DoctorZombieEntity;
import com.craftingdead.core.entity.monster.FastZombieEntity;
import com.craftingdead.core.entity.monster.GiantZombieEntity;
import com.craftingdead.core.entity.monster.PoliceZombieEntity;
import com.craftingdead.core.entity.monster.TankZombieEntity;
import com.craftingdead.core.entity.monster.WeakZombieEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;

/**
 * We don't use {@link DeferredRegister} here because of the way {@link SpawnEggItem}s are
 * registered.
 */
public class ModEntityTypes {

  private static final List<EntityType<?>> toRegister = new ArrayList<>();

  public static EntityType<AdvancedZombieEntity> advancedZombie;
  public static EntityType<FastZombieEntity> fastZombie;
  public static EntityType<TankZombieEntity> tankZombie;
  public static EntityType<WeakZombieEntity> weakZombie;
  public static EntityType<AdvancedZombieEntity> policeZombie;
  public static EntityType<AdvancedZombieEntity> doctorZombie;
  public static EntityType<GiantZombieEntity> giantZombie;

  public static EntityType<C4ExplosiveEntity> c4Explosive;
  public static EntityType<DecoyGrenadeEntity> decoyGrenade;
  public static EntityType<FireGrenadeEntity> fireGrenade;
  public static EntityType<FlashGrenadeEntity> flashGrenade;
  public static EntityType<FragGrenadeEntity> fragGrenade;
  public static EntityType<PipeGrenadeEntity> pipeGrenade;
  public static EntityType<SmokeGrenadeEntity> smokeGrenade;

  public static EntityType<SupplyDropEntity> supplyDrop;

  public static void initialize() {
    c4Explosive = add("c4_explosive",
        EntityType.Builder
            .<C4ExplosiveEntity>of(C4ExplosiveEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .sized(0.65F, 0.25F)
            .setShouldReceiveVelocityUpdates(false));

    decoyGrenade = add("decoy_grenade",
        EntityType.Builder
            .<DecoyGrenadeEntity>of(DecoyGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .sized(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    fireGrenade = add("fire_grenade",
        EntityType.Builder
            .<FireGrenadeEntity>of(FireGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .sized(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    flashGrenade = add("flash_grenade",
        EntityType.Builder
            .<FlashGrenadeEntity>of(FlashGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .sized(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    fragGrenade = add("frag_grenade",
        EntityType.Builder
            .<FragGrenadeEntity>of(FragGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .sized(0.25F, 0.25F)
            .setShouldReceiveVelocityUpdates(false));

    pipeGrenade = add("pipe_grenade",
        EntityType.Builder
            .<PipeGrenadeEntity>of(PipeGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .sized(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    smokeGrenade = add("smoke_grenade",
        EntityType.Builder
            .<SmokeGrenadeEntity>of(SmokeGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .sized(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    advancedZombie = add("advanced_zombie",
        EntityType.Builder
            .<AdvancedZombieEntity>of(AdvancedZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    fastZombie = add("fast_zombie",
        EntityType.Builder
            .<FastZombieEntity>of(FastZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    tankZombie = add("tank_zombie",
        EntityType.Builder
            .<TankZombieEntity>of(TankZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    weakZombie = add("weak_zombie",
        EntityType.Builder
            .<WeakZombieEntity>of(WeakZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    policeZombie = add("police_zombie",
        EntityType.Builder
            .<AdvancedZombieEntity>of(PoliceZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    doctorZombie = add("doctor_zombie",
        EntityType.Builder
            .<AdvancedZombieEntity>of(DoctorZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    giantZombie = add("giant_zombie",
        EntityType.Builder
            .<GiantZombieEntity>of(GiantZombieEntity::new,
                EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .sized(3.6F, 12.0F)
            .setShouldReceiveVelocityUpdates(false));

    supplyDrop = add("supply_drop", EntityType.Builder
        .<SupplyDropEntity>of(SupplyDropEntity::new, EntityClassification.MISC));

    // Spawn placement rules
    EntitySpawnPlacementRegistry.register(ModEntityTypes.advancedZombie,
        EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);

    EntitySpawnPlacementRegistry.register(ModEntityTypes.fastZombie,
        EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);

    EntitySpawnPlacementRegistry.register(ModEntityTypes.tankZombie,
        EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);

    EntitySpawnPlacementRegistry.register(ModEntityTypes.weakZombie,
        EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);
  }

  public static void registerAll(RegistryEvent.Register<EntityType<?>> event) {
    toRegister.forEach(event.getRegistry()::register);
  }

  private static <T extends Entity> EntityType<T> add(String registryName,
      EntityType.Builder<T> builder) {
    ResourceLocation resourceLocation = new ResourceLocation(CraftingDead.ID, registryName);
    EntityType<T> entityType = builder.build(resourceLocation.toString());
    entityType.setRegistryName(resourceLocation);
    toRegister.add(entityType);
    return entityType;
  }
}
