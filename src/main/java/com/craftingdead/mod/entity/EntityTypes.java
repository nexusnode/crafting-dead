package com.craftingdead.mod.entity;

import com.craftingdead.mod.CraftingDead;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypes {

  public static final EntityType<CorpseEntity> CORPSE = null;

  @SubscribeEvent
  public static void handle(RegistryEvent.Register<EntityType<?>> event) {
    event.getRegistry()
        .registerAll(EntityType.Builder.create(CorpseEntity::new, EntityClassification.MISC) //
            .setTrackingRange(64) //
            .setUpdateInterval(10) //
            .setShouldReceiveVelocityUpdates(false) //
            .setCustomClientFactory(CorpseEntity::new) //
            .build(new ResourceLocation(CraftingDead.MOD_ID, "corpse").toString()) //
            .setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, "corpse")));
  }
}
