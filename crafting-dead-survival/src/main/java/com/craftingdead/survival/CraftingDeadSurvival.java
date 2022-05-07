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

package com.craftingdead.survival;

import java.util.ListIterator;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.action.item.EntityItemAction;
import com.craftingdead.core.world.entity.extension.BasicLivingExtension;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.core.world.item.clothing.Clothing;
import com.craftingdead.survival.client.ClientDist;
import com.craftingdead.survival.data.SurvivalItemTagsProvider;
import com.craftingdead.survival.data.SurvivalRecipeProvider;
import com.craftingdead.survival.data.loot.SurvivalLootTableProvider;
import com.craftingdead.survival.particles.SurvivalParticleTypes;
import com.craftingdead.survival.server.ServerDist;
import com.craftingdead.survival.world.action.SurvivalActionTypes;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import com.craftingdead.survival.world.entity.SurvivalEntityTypes;
import com.craftingdead.survival.world.entity.SurvivalPlayerHandler;
import com.craftingdead.survival.world.entity.SurvivalZombieHandler;
import com.craftingdead.survival.world.entity.monster.AdvancedZombie;
import com.craftingdead.survival.world.entity.monster.DoctorZombieEntity;
import com.craftingdead.survival.world.entity.monster.FastZombie;
import com.craftingdead.survival.world.entity.monster.GiantZombie;
import com.craftingdead.survival.world.entity.monster.PoliceZombieEntity;
import com.craftingdead.survival.world.entity.monster.TankZombie;
import com.craftingdead.survival.world.entity.monster.WeakZombie;
import com.craftingdead.survival.world.item.SurvivalItems;
import com.craftingdead.survival.world.item.enchantment.SurvivalEnchantments;
import com.craftingdead.survival.world.level.block.SurvivalBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(CraftingDeadSurvival.ID)
public class CraftingDeadSurvival {

  public static final String ID = "craftingdeadsurvival";

  private static final String H_CD_SERVER_CORE_ID = "hcdservercore";

  public static final ServerConfig serverConfig;
  public static final ForgeConfigSpec serverConfigSpec;

  static {
    final Pair<ServerConfig, ForgeConfigSpec> serverConfigPair =
        new ForgeConfigSpec.Builder().configure(ServerConfig::new);
    serverConfigSpec = serverConfigPair.getRight();
    serverConfig = serverConfigPair.getLeft();
  }

  private static CraftingDeadSurvival instance;

  private final ModDist modDist;

  public CraftingDeadSurvival() {
    instance = this;

    this.modDist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleEntityAttributeCreation);
    modEventBus.addListener(this::handleGatherData);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverConfigSpec);

    MinecraftForge.EVENT_BUS.register(this);

    SurvivalEnchantments.deferredRegister.register(modEventBus);
    SurvivalActionTypes.deferredRegister.register(modEventBus);
    SurvivalItems.deferredRegister.register(modEventBus);
    SurvivalMobEffects.deferredRegister.register(modEventBus);
    SurvivalEntityTypes.deferredRegister.register(modEventBus);
    SurvivalParticleTypes.deferredRegister.register(modEventBus);
    SurvivalBlocks.deferredRegister.register(modEventBus);
  }

  public ModDist getModDist() {
    return this.modDist;
  }

  public CraftingDeadSurvival getInstance() {
    return instance;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    BrewingRecipeRegistry.addRecipe(Ingredient.of(ModItems.SYRINGE.get()),
        Ingredient.of(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE),
        new ItemStack(SurvivalItems.CURE_SYRINGE.get()));

    registerEntitySpawnPlacements();
  }

  private static void registerEntitySpawnPlacements() {
    SpawnPlacements.register(SurvivalEntityTypes.ADVANCED_ZOMBIE.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        AdvancedZombie::checkAdvancedZombieSpawnRules);

    SpawnPlacements.register(SurvivalEntityTypes.FAST_ZOMBIE.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        AdvancedZombie::checkAdvancedZombieSpawnRules);

    SpawnPlacements.register(SurvivalEntityTypes.TANK_ZOMBIE.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        AdvancedZombie::checkAdvancedZombieSpawnRules);

    SpawnPlacements.register(SurvivalEntityTypes.WEAK_ZOMBIE.get(),
        SpawnPlacements.Type.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        AdvancedZombie::checkAdvancedZombieSpawnRules);
  }

  private void handleEntityAttributeCreation(EntityAttributeCreationEvent event) {
    event.put(SurvivalEntityTypes.ADVANCED_ZOMBIE.get(),
        AdvancedZombie.createAttributes().build());
    event.put(SurvivalEntityTypes.DOCTOR_ZOMBIE.get(),
        DoctorZombieEntity.createAttributes().build());
    event.put(SurvivalEntityTypes.FAST_ZOMBIE.get(),
        FastZombie.createAttributes().build());
    event.put(SurvivalEntityTypes.GIANT_ZOMBIE.get(),
        GiantZombie.createAttributes().build());
    event.put(SurvivalEntityTypes.POLICE_ZOMBIE.get(),
        PoliceZombieEntity.createAttributes().build());
    event.put(SurvivalEntityTypes.TANK_ZOMBIE.get(),
        TankZombie.createAttributes().build());
    event.put(SurvivalEntityTypes.WEAK_ZOMBIE.get(),
        WeakZombie.createAttributes().build());
  }

  private void handleGatherData(GatherDataEvent event) {
    DataGenerator dataGenerator = event.getGenerator();
    if (event.includeServer()) {
      dataGenerator.addProvider(new SurvivalItemTagsProvider(dataGenerator,
          new ForgeBlockTagsProvider(dataGenerator, event.getExistingFileHelper()),
          event.getExistingFileHelper()));
      dataGenerator.addProvider(new SurvivalRecipeProvider(dataGenerator));
      dataGenerator.addProvider(new SurvivalLootTableProvider(dataGenerator));
    }
  }

  // ================================================================================
  // Common Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handlePerformAction(LivingExtensionEvent.PerformAction<EntityItemAction<?>> event) {
    var action = event.getAction();
    LivingExtension<?, ?> target = action.getSelectedTarget();
    if (!event.getLiving().getLevel().isClientSide()
        && action.getType() == ActionTypes.USE_SYRINGE.get()) {
      SurvivalActionTypes.USE_SYRINGE_ON_ZOMBIE.get()
          .createEntityAction(event.getLiving(), target, action.getHand())
          .ifPresent(newAction -> {
            event.setCanceled(true);
            event.getLiving().performAction(newAction, true);
          });
    }
  }

  @SubscribeEvent
  public void handleMissingBlockMappings(RegistryEvent.MissingMappings<Block> event) {
    event.getMappings(H_CD_SERVER_CORE_ID).forEach(mapping -> {
      Block newBlock = mapping.registry.getValue(new ResourceLocation(ID, mapping.key.getPath()));
      if (newBlock != null) {
        mapping.remap(newBlock);
      }
    });
  }

  @SubscribeEvent
  public void handleMissingItemMappings(RegistryEvent.MissingMappings<Item> event) {
    event.getMappings(H_CD_SERVER_CORE_ID).forEach(mapping -> {
      Item newItem = mapping.registry.getValue(new ResourceLocation(ID, mapping.key.getPath()));
      if (newItem != null) {
        mapping.remap(newItem);
      }
    });
  }

  @SubscribeEvent
  public void handleAttachLivingExtensions(LivingExtensionEvent.Load event) {
    if (event.getLiving() instanceof PlayerExtension<?> player) {
      player.registerHandler(SurvivalPlayerHandler.TYPE, new SurvivalPlayerHandler(player));
    } else if (event.getLiving().getEntity() instanceof AdvancedZombie
        && event.getLiving() instanceof BasicLivingExtension living) {
      living.registerHandler(SurvivalZombieHandler.TYPE, new SurvivalZombieHandler());
    }
  }

  @SubscribeEvent
  public void handleRightClickItem(PlayerInteractEvent.RightClickItem event) {
    if (!event.getWorld().isClientSide()
        && event.getItemStack().getCapability(Clothing.CAPABILITY).isPresent()) {
      var extension = PlayerExtension.getOrThrow(event.getPlayer());
      extension.performAction(
          SurvivalActionTypes.SHRED_CLOTHING.get().decode(extension, null), true);
    }
  }

  @SubscribeEvent
  public void handleGunHitEntity(GunEvent.HitEntity event) {
    event.getTarget().getCapability(LivingExtension.CAPABILITY)
        .resolve()
        .flatMap(living -> living.getHandler(SurvivalPlayerHandler.TYPE))
        .ifPresent(playerHandler -> {
          float enchantmentPct =
              EnchantmentHelper.getItemEnchantmentLevel(SurvivalEnchantments.INFECTION.get(),
                  event.getItemStack())
                  / (float) SurvivalEnchantments.INFECTION.get().getMaxLevel();
          playerHandler.infect(enchantmentPct);
        });
  }

  @SubscribeEvent
  public void handleBiomeLoading(BiomeLoadingEvent event) {
    if (!serverConfig.zombiesEnabled.get()) {
      return;
    }

    ListIterator<MobSpawnSettings.SpawnerData> iterator =
        event.getSpawns().getSpawner(MobCategory.MONSTER).listIterator();
    while (iterator.hasNext()) {
      MobSpawnSettings.SpawnerData spawnEntry = iterator.next();
      if (spawnEntry.type == EntityType.ZOMBIE) {
        if (serverConfig.advancedZombiesEnabled.get()) {
          iterator.add(new MobSpawnSettings.SpawnerData(
              SurvivalEntityTypes.ADVANCED_ZOMBIE.get(),
              serverConfig.advancedZombieSpawnWeight.get(),
              serverConfig.advancedZombieMinSpawn.get(),
              serverConfig.advancedZombieMaxSpawn.get()));
        }
        if (serverConfig.fastZombiesEnabled.get()) {
          iterator.add(new MobSpawnSettings.SpawnerData(
              SurvivalEntityTypes.FAST_ZOMBIE.get(),
              serverConfig.fastZombieSpawnWeight.get(),
              serverConfig.fastZombieMinSpawn.get(),
              serverConfig.fastZombieMaxSpawn.get()));
        }
        if (serverConfig.tankZombiesEnabled.get()) {
          iterator.add(new MobSpawnSettings.SpawnerData(
              SurvivalEntityTypes.TANK_ZOMBIE.get(),
              serverConfig.tankZombieSpawnWeight.get(),
              serverConfig.tankZombieMinSpawn.get(),
              serverConfig.tankZombieMaxSpawn.get()));
        }
        if (serverConfig.weakZombiesEnabled.get()) {
          iterator.add(new MobSpawnSettings.SpawnerData(
              SurvivalEntityTypes.WEAK_ZOMBIE.get(),
              serverConfig.weakZombieSpawnWeight.get(),
              serverConfig.weakZombieMinSpawn.get(),
              serverConfig.weakZombieMaxSpawn.get()));
        }
      }
    }
  }
}
