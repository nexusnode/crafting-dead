package com.craftingdead.core.entity;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.entity.grenade.C4ExplosiveEntity;
import com.craftingdead.core.entity.grenade.DecoyGrenadeEntity;
import com.craftingdead.core.entity.grenade.FireGrenadeEntity;
import com.craftingdead.core.entity.grenade.FlashGrenadeEntity;
import com.craftingdead.core.entity.grenade.FragGrenadeEntity;
import com.craftingdead.core.entity.grenade.GasGrenadeEntity;
import com.craftingdead.core.entity.grenade.PipeGrenadeEntity;
import com.craftingdead.core.entity.grenade.SmokeGrenadeEntity;
import com.craftingdead.core.entity.monster.AdvancedZombieEntity;
import com.craftingdead.core.entity.monster.FastZombieEntity;
import com.craftingdead.core.entity.monster.GiantZombieEntity;
import com.craftingdead.core.entity.monster.TankZombieEntity;
import com.craftingdead.core.entity.monster.WeakZombieEntity;
import com.craftingdead.core.item.ModItems;
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

  public static EntityType<C4ExplosiveEntity> c4Explosive;
  public static EntityType<DecoyGrenadeEntity> decoyGrenade;
  public static EntityType<FireGrenadeEntity> fireGrenade;
  public static EntityType<FlashGrenadeEntity> flashGrenade;
  public static EntityType<FragGrenadeEntity> fragGrenade;
  public static EntityType<GasGrenadeEntity> gasGrenade;
  public static EntityType<PipeGrenadeEntity> pipeGrenade;
  public static EntityType<SmokeGrenadeEntity> smokeGrenade;

  public static EntityType<SupplyDropEntity> supplyDrop;

  public static void initialize() {
    c4Explosive = add("c4_explosive",
        EntityType.Builder
            .<C4ExplosiveEntity>create(C4ExplosiveEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.65F, 0.25F)
            .setShouldReceiveVelocityUpdates(false));

    decoyGrenade = add("decoy_grenade",
        EntityType.Builder
            .<DecoyGrenadeEntity>create(DecoyGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    fireGrenade = add("fire_grenade",
        EntityType.Builder
            .<FireGrenadeEntity>create(FireGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    flashGrenade = add("flash_grenade",
        EntityType.Builder
            .<FlashGrenadeEntity>create(FlashGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    fragGrenade = add("frag_grenade",
        EntityType.Builder
            .<FragGrenadeEntity>create(FragGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.25F, 0.25F)
            .setShouldReceiveVelocityUpdates(false));

    gasGrenade = add("gas_grenade",
        EntityType.Builder
            .<GasGrenadeEntity>create(GasGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    pipeGrenade = add("pipe_grenade",
        EntityType.Builder
            .<PipeGrenadeEntity>create(PipeGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

    smokeGrenade = add("smoke_grenade",
        EntityType.Builder
            .<SmokeGrenadeEntity>create(SmokeGrenadeEntity::new, EntityClassification.MISC)
            .setTrackingRange(64)
            .setUpdateInterval(4)
            .size(0.25F, 0.5F)
            .setShouldReceiveVelocityUpdates(false));

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
