package com.craftingdead.mod.entity;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import com.craftingdead.mod.entity.monster.FastZombieEntity;
import com.craftingdead.mod.entity.monster.GiantZombieEntity;
import com.craftingdead.mod.entity.monster.TankZombieEntity;
import com.craftingdead.mod.entity.monster.WeakZombieEntity;
import com.craftingdead.mod.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;

public class ModEntityTypes {

  private static final List<EntityType<?>> toRegister = new ArrayList<>();

  public static EntityType<CorpseEntity> corpse;

  public static EntityType<AdvancedZombieEntity> advancedZombie;
  public static EntityType<FastZombieEntity> fastZombie;
  public static EntityType<TankZombieEntity> tankZombie;
  public static EntityType<WeakZombieEntity> weakZombie;
  public static EntityType<AdvancedZombieEntity> policeZombie;
  public static EntityType<AdvancedZombieEntity> doctorZombie;
  public static EntityType<GiantZombieEntity> giantZombie;

  public static EntityType<GrenadeEntity> grenade;

  public static EntityType<SupplyDropEntity> supplyDrop;

  public static void initialize() {
    grenade = add("fire_grenade",
        EntityType.Builder
            .<GrenadeEntity>create(GrenadeEntity::new, EntityClassification.MISC)
            .size(0.25F, 0.25F));

    corpse = add("corpse",
        EntityType.Builder
            .<CorpseEntity>create(CorpseEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(10)
            .setShouldReceiveVelocityUpdates(false));

    advancedZombie = add("advanced_zombie",
        EntityType.Builder
            .<AdvancedZombieEntity>create(AdvancedZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .size(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    fastZombie = add("fast_zombie",
        EntityType.Builder
            .<FastZombieEntity>create(FastZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .size(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    tankZombie = add("tank_zombie",
        EntityType.Builder
            .<TankZombieEntity>create(TankZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .size(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    weakZombie = add("weak_zombie",
        EntityType.Builder
            .<WeakZombieEntity>create(WeakZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .size(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    policeZombie = add("police_zombie", EntityType.Builder
        .<AdvancedZombieEntity>create((type, world) -> new AdvancedZombieEntity(type, world,
            ModItems.G18::get, ModItems.POLICE_CLOTHING::get, null), EntityClassification.MONSTER)
        .setTrackingRange(64)
        .setUpdateInterval(3)
        .size(0.6F, 1.95F)
        .setShouldReceiveVelocityUpdates(false));

    doctorZombie = add("doctor_zombie",
        EntityType.Builder
            .<AdvancedZombieEntity>create(
                (type, world) -> new AdvancedZombieEntity(type, world, null,
                    ModItems.DOCTOR_CLOTHING::get, ModItems.DOCTOR_MASK::get),
                EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .size(0.6F, 1.95F)
            .setShouldReceiveVelocityUpdates(false));

    giantZombie = add("giant_zombie",
        EntityType.Builder
            .<GiantZombieEntity>create(
                (type, world) -> new GiantZombieEntity(type, world, ModItems.M4A1::get,
                    ModItems.ARMY_CLOTHING::get, ModItems.ARMY_HELMET::get),
                EntityClassification.MONSTER)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .size(3.6F, 12.0F)
            .setShouldReceiveVelocityUpdates(false));

    supplyDrop = add("supply_drop", EntityType.Builder
        .<SupplyDropEntity>create(SupplyDropEntity::new, EntityClassification.MISC));

    // Spawn placement rules
    EntitySpawnPlacementRegistry
        .register(ModEntityTypes.advancedZombie,
            EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);

    EntitySpawnPlacementRegistry
        .register(ModEntityTypes.fastZombie, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);

    EntitySpawnPlacementRegistry
        .register(ModEntityTypes.tankZombie, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);

    EntitySpawnPlacementRegistry
        .register(ModEntityTypes.weakZombie, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AdvancedZombieEntity::areSpawnConditionsMet);
  }

  public static void register(RegistryEvent.Register<EntityType<?>> event) {
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
