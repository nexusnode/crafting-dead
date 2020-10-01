/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core;

import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.action.ActionTypes;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.data.ModItemTagsProvider;
import com.craftingdead.core.data.ModLootTableProvider;
import com.craftingdead.core.data.ModRecipeProvider;
import com.craftingdead.core.enchantment.ModEnchantments;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.game.GameTypes;
import com.craftingdead.core.inventory.container.ModContainerTypes;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.item.crafting.ModRecipeSerializers;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.particle.ModParticleTypes;
import com.craftingdead.core.potion.ModEffects;
import com.craftingdead.core.server.LogicalServer;
import com.craftingdead.core.server.ServerDist;
import com.craftingdead.core.util.ArbitraryTooltips;
import com.craftingdead.core.util.ModSoundEvents;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(CraftingDead.ID)
public class CraftingDead {

  public static final String ID = "craftingdead";

  public static final String VERSION;

  public static final String DISPLAY_NAME;

  public static final CommonConfig commonConfig;
  public static final ForgeConfigSpec commonConfigSpec;

  static {
    VERSION =
        JarVersionLookupHandler.getImplementationVersion(CraftingDead.class).orElse("[version]");
    assert VERSION != null;
    DISPLAY_NAME =
        JarVersionLookupHandler.getImplementationTitle(CraftingDead.class).orElse("[display_name]");
    assert DISPLAY_NAME != null;

    final Pair<CommonConfig, ForgeConfigSpec> commonConfigPair =
        new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    commonConfigSpec = commonConfigPair.getRight();
    commonConfig = commonConfigPair.getLeft();
  }

  private static final String TRAVELERS_BACKPACK_ID = "travelersbackpack";

  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger();

  /**
   * Singleton.
   */
  private static CraftingDead instance;

  /**
   * Mod distribution.
   */
  private final IModDist modDist;

  private boolean travelersBackpacksLoaded;

  @Nullable
  private LogicalServer logicalServer;

  public CraftingDead() {
    instance = this;

    this.modDist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.register(this);

    ModEntityTypes.initialize();
    modEventBus.addGenericListener(EntityType.class, ModEntityTypes::registerAll);

    ModItems.ITEMS.register(modEventBus);
    ModSoundEvents.SOUND_EVENTS.register(modEventBus);
    ModContainerTypes.CONTAINERS.register(modEventBus);
    ModEffects.EFFECTS.register(modEventBus);
    ModEnchantments.ENCHANTMENTS.register(modEventBus);
    ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
    ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

    ActionTypes.ACTION_TYPES.makeRegistry("action_type", RegistryBuilder::new);
    ActionTypes.ACTION_TYPES.register(modEventBus);

    GameTypes.GAME_TYPES.makeRegistry("game_type", RegistryBuilder::new);
    GameTypes.GAME_TYPES.register(modEventBus);

    // Should be registered after ITEMS registration
    modEventBus.addGenericListener(Item.class, ArbitraryTooltips::registerAll);

    MinecraftForge.EVENT_BUS.register(this);

    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfigSpec);
  }

  public IModDist getModDist() {
    return this.modDist;
  }

  public boolean isTravelersBackpacksLoaded() {
    return this.travelersBackpacksLoaded;
  }

  @Nullable
  public LogicalServer getLogicalServer() {
    return this.logicalServer;
  }

  public static CraftingDead getInstance() {
    return instance;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public void handleCommonSetup(FMLCommonSetupEvent event) {
    logger.info("Starting {}, version {}", DISPLAY_NAME, VERSION);
    NetworkChannel.loadChannels();
    logger.info("Registering capabilities");
    ModCapabilities.registerCapabilities();
    net.minecraftforge.fml.DeferredWorkQueue.runLater(() -> {
      BrewingRecipeRegistry
          .addRecipe(Ingredient.fromItems(ModItems.SYRINGE.get()),
              Ingredient.fromTag(Tags.Items.DUSTS_REDSTONE),
              new ItemStack(ModItems.ADRENALINE_SYRINGE.get()));
      BrewingRecipeRegistry
          .addRecipe(Ingredient.fromItems(ModItems.SYRINGE.get()),
              Ingredient.fromItems(Items.ENCHANTED_GOLDEN_APPLE),
              new ItemStack(ModItems.CURE_SYRINGE.get()));
    });

    this.travelersBackpacksLoaded = ModList.get().isLoaded(TRAVELERS_BACKPACK_ID);
    if (this.travelersBackpacksLoaded) {
      logger.info("Adding integration for " + TRAVELERS_BACKPACK_ID);
    }
  }

  @SubscribeEvent
  public void handleGatherData(GatherDataEvent event) {
    DataGenerator dataGenerator = event.getGenerator();
    dataGenerator.addProvider(new ModItemTagsProvider(dataGenerator));
    dataGenerator.addProvider(new ModRecipeProvider(dataGenerator));
    dataGenerator.addProvider(new ModLootTableProvider(dataGenerator));
  }

  @SubscribeEvent
  public void handleServerAboutToStart(FMLServerStartingEvent event) {
    this.logicalServer = this.modDist.createLogicalServer(event.getServer());
    this.logicalServer.init();
    MinecraftForge.EVENT_BUS.register(this.logicalServer);
  }

  @SubscribeEvent
  public void handleServerStopping(FMLServerStoppingEvent event) {
    MinecraftForge.EVENT_BUS.unregister(this.logicalServer);
    this.logicalServer = null;
  }
}
