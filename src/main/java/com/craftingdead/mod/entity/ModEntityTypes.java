package com.craftingdead.mod.entity;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import com.craftingdead.mod.entity.monster.FastZombieEntity;
import com.craftingdead.mod.entity.monster.TankZombieEntity;
import com.craftingdead.mod.entity.monster.WeakZombieEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CraftingDead.ID)
@Mod.EventBusSubscriber(modid = CraftingDead.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {

  public static final EntityType<CorpseEntity> CORPSE = null;

  public static final EntityType<AdvancedZombieEntity> ADVANCED_ZOMBIE = null;
  public static final EntityType<FastZombieEntity> FAST_ZOMBIE = null;
  public static final EntityType<TankZombieEntity> TANK_ZOMBIE = null;
  public static final EntityType<WeakZombieEntity> WEAK_ZOMBIE = null;

  @SubscribeEvent
  public static void handle(RegistryEvent.Register<EntityType<?>> event) {
    event.getRegistry()
        .register(EntityType.Builder.create(CorpseEntity::new, EntityClassification.MISC) //
            .setTrackingRange(64) //
            .setUpdateInterval(10) //
            .setShouldReceiveVelocityUpdates(false) //
            .setCustomClientFactory(CorpseEntity::new) //
            .build(new ResourceLocation(CraftingDead.ID, "corpse").toString()) //
            .setRegistryName(new ResourceLocation(CraftingDead.ID, "corpse")));
    event.getRegistry()
        .register(EntityType.Builder
            .<AdvancedZombieEntity>create(AdvancedZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64) //
            .setUpdateInterval(3) //
            .setShouldReceiveVelocityUpdates(false) //
            .build(new ResourceLocation(CraftingDead.ID, "advanced_zombie").toString())
            .setRegistryName(new ResourceLocation(CraftingDead.ID, "advanced_zombie")));
    event.getRegistry()
        .register(EntityType.Builder
            .<FastZombieEntity>create(FastZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64) //
            .setUpdateInterval(3) //
            .setShouldReceiveVelocityUpdates(false) //
            .build(new ResourceLocation(CraftingDead.ID, "fast_zombie").toString())
            .setRegistryName(new ResourceLocation(CraftingDead.ID, "fast_zombie")));
    event.getRegistry()
        .register(EntityType.Builder
            .<TankZombieEntity>create(TankZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64) //
            .setUpdateInterval(3) //
            .setShouldReceiveVelocityUpdates(false) //
            .build(new ResourceLocation(CraftingDead.ID, "tank_zombie").toString())
            .setRegistryName(new ResourceLocation(CraftingDead.ID, "tank_zombie")));
    event.getRegistry()
        .register(EntityType.Builder
            .<WeakZombieEntity>create(WeakZombieEntity::new, EntityClassification.MONSTER)
            .setTrackingRange(64) //
            .setUpdateInterval(3) //
            .setShouldReceiveVelocityUpdates(false) //
            .build(new ResourceLocation(CraftingDead.ID, "weak_zombie").toString())
            .setRegistryName(new ResourceLocation(CraftingDead.ID, "weak_zombie")));
  }
}
