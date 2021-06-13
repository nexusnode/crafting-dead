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

package com.craftingdead.survival;

import java.util.ListIterator;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.action.item.ItemAction;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.ModItems;
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
import com.craftingdead.survival.world.entity.monster.AdvancedZombieEntity;
import com.craftingdead.survival.world.entity.monster.DoctorZombieEntity;
import com.craftingdead.survival.world.entity.monster.FastZombieEntity;
import com.craftingdead.survival.world.entity.monster.GiantZombieEntity;
import com.craftingdead.survival.world.entity.monster.PoliceZombieEntity;
import com.craftingdead.survival.world.entity.monster.TankZombieEntity;
import com.craftingdead.survival.world.entity.monster.WeakZombieEntity;
import com.craftingdead.survival.world.item.SurvivalItems;
import com.craftingdead.survival.world.item.enchantment.SurvivalEnchantments;
import com.craftingdead.survival.world.level.block.SurvivalBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleEntityAttributeCreation);
    modEventBus.addListener(this::handleGatherData);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverConfigSpec);

    MinecraftForge.EVENT_BUS.register(this);

    SurvivalEntityTypes.initialize();
    modEventBus.addGenericListener(EntityType.class, SurvivalEntityTypes::registerAll);

    SurvivalEnchantments.ENCHANTMENTS.register(modEventBus);
    SurvivalActionTypes.ACTION_TYPES.register(modEventBus);
    SurvivalItems.ITEMS.register(modEventBus);
    SurvivalMobEffects.MOB_EFFECTS.register(modEventBus);
    SurvivalEntityTypes.ENTITY_TYPES.register(modEventBus);
    SurvivalParticleTypes.PARTICLE_TYPES.register(modEventBus);
    SurvivalBlocks.BLOCKS.register(modEventBus);
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
  }

  private void handleEntityAttributeCreation(EntityAttributeCreationEvent event) {
    event.put(SurvivalEntityTypes.advancedZombie,
        AdvancedZombieEntity.registerAttributes().build());
    event.put(SurvivalEntityTypes.doctorZombie, DoctorZombieEntity.registerAttributes().build());
    event.put(SurvivalEntityTypes.fastZombie, FastZombieEntity.registerAttributes().build());
    event.put(SurvivalEntityTypes.giantZombie, GiantZombieEntity.registerAttributes().build());
    event.put(SurvivalEntityTypes.policeZombie, PoliceZombieEntity.registerAttributes().build());
    event.put(SurvivalEntityTypes.tankZombie, TankZombieEntity.registerAttributes().build());
    event.put(SurvivalEntityTypes.weakZombie, WeakZombieEntity.registerAttributes().build());
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
  public void handlePerformAction(LivingExtensionEvent.PerformAction<ItemAction> event) {
    Action action = event.getAction();
    LivingExtension<?, ?> target = action.getTarget().orElse(null);
    if (action.getType() == ActionTypes.USE_SYRINGE.get()
        && target != null
        && target.getEntity() instanceof ZombieEntity) {
      event.setCanceled(true);
      event.getLiving().performAction(
          SurvivalActionTypes.USE_SYRINGE_ON_ZOMBIE.get().createAction(action.getPerformer(),
              target),
          true);
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
    if (event.getLiving() instanceof PlayerExtension
        && !event.getLiving().getHandler(SurvivalPlayerHandler.ID).isPresent()) {
      PlayerExtension<?> player = (PlayerExtension<?>) event.getLiving();
      player.registerHandler(SurvivalPlayerHandler.ID, new SurvivalPlayerHandler(player));
    }
  }

  @SubscribeEvent
  public void handleRightClickItem(PlayerInteractEvent.RightClickItem event) {
    if (!event.getWorld().isClientSide()
        && event.getItemStack().getCapability(Capabilities.CLOTHING).isPresent()) {
      PlayerExtension<?> extension = PlayerExtension.getExpected(event.getPlayer());
      extension.performAction(
          SurvivalActionTypes.SHRED_CLOTHING.get().createAction(extension, null), true);
    }
  }

  @SubscribeEvent
  public void handleGunHitEntity(GunEvent.HitEntity event) {
    event.getTarget().getCapability(Capabilities.LIVING)
        .resolve()
        .flatMap(living -> living.getHandler(SurvivalPlayerHandler.ID))
        .map(living -> (SurvivalPlayerHandler) living)
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
    if (serverConfig.disableZombies.get()) {
      return;
    }

    ListIterator<MobSpawnInfo.Spawners> iterator =
        event.getSpawns().getSpawner(EntityClassification.MONSTER).listIterator();
    while (iterator.hasNext()) {
      MobSpawnInfo.Spawners spawnEntry = iterator.next();
      if (spawnEntry.type == EntityType.ZOMBIE) {
        iterator.add(new MobSpawnInfo.Spawners(
            SurvivalEntityTypes.advancedZombie, spawnEntry.weight * 3, 2, 8));
        iterator.add(new MobSpawnInfo.Spawners(
            SurvivalEntityTypes.fastZombie, spawnEntry.weight / 2, 2, 4));
        iterator.add(new MobSpawnInfo.Spawners(
            SurvivalEntityTypes.tankZombie, spawnEntry.weight / 2, 2, 4));
        iterator.add(new MobSpawnInfo.Spawners(
            SurvivalEntityTypes.advancedZombie, spawnEntry.weight, 3, 8));
        iterator.add(new MobSpawnInfo.Spawners(
            SurvivalEntityTypes.weakZombie, spawnEntry.weight, 3, 12));
      }
    }
  }
}
