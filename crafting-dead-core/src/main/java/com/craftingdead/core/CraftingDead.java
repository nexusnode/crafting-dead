package com.craftingdead.core;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.action.ActionType;
import com.craftingdead.core.action.ActionTypes;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.hydration.DefaultHydration;
import com.craftingdead.core.capability.hydration.PresetHydration;
import com.craftingdead.core.capability.living.DefaultLiving;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.player.ServerPlayer;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.data.ModItemTagsProvider;
import com.craftingdead.core.data.ModLootTableProvider;
import com.craftingdead.core.data.ModRecipeProvider;
import com.craftingdead.core.enchantment.ModEnchantments;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.inventory.container.ModContainerTypes;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.item.crafting.ModRecipeSerializers;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.particle.ModParticleTypes;
import com.craftingdead.core.potion.ModEffects;
import com.craftingdead.core.server.ServerDist;
import com.craftingdead.core.util.ArbitraryTooltips;
import com.craftingdead.core.util.ModSoundEvents;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;
import net.minecraftforge.registries.IForgeRegistry;
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

  private IForgeRegistry<ActionType<?>> actionTypeRegistry;

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

  public IForgeRegistry<ActionType<?>> getActionTypeRegistry() {
    return this.actionTypeRegistry;
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

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleLivingUpdate(LivingUpdateEvent event) {
    if (!(event.getEntityLiving() instanceof PlayerEntity)) {
      event.getEntityLiving().getCapability(ModCapabilities.LIVING)
          .ifPresent(ILiving::tick);
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    switch (event.phase) {
      case END:
        event.player.getCapability(ModCapabilities.LIVING).ifPresent(ILiving::tick);
        break;
      default:
        break;
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingSetTarget(LivingSetAttackTargetEvent event) {
    if (event.getTarget() != null && event.getEntityLiving() instanceof MobEntity) {
      MobEntity mobEntity = (MobEntity) event.getEntityLiving();
      if (mobEntity.isPotionActive(ModEffects.FLASH_BLINDNESS.get())) {
        mobEntity.setAttackTarget(null);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDeath(LivingDeathEvent event) {
    if (event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .map(living -> living.onDeath(event.getSource()))
        .orElse(false)
        || (event.getSource().getTrueSource() != null && event
            .getSource()
            .getTrueSource()
            .getCapability(ModCapabilities.LIVING)
            .map(living -> living.onKill(event.getEntity()))
            .orElse(false))) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDrops(LivingDropsEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onDeathDrops(event.getSource(), event.getDrops())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingAttack(LivingAttackEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onAttacked(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDamage(LivingDamageEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setAmount(living.onDamaged(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent
  public void handlePlayerClone(PlayerEvent.Clone event) {
    event
        .getPlayer()
        .getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof ServerPlayer)
        .<ServerPlayer>cast()
        .ifPresent(player -> {
          event
              .getOriginal()
              .getCapability(ModCapabilities.LIVING)
              .filter(living -> living instanceof ServerPlayer)
              .<ServerPlayer>cast()
              .ifPresent(that -> player.copyFrom(that, event.isWasDeath()));
        });
  }

  @SubscribeEvent
  public void handleAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
    if (!(event.getObject() instanceof PlayerEntity) && event.getObject() instanceof LivingEntity) {
      event.addCapability(new ResourceLocation(CraftingDead.ID, "living"),
          new SerializableCapabilityProvider<>(
              new DefaultLiving<>((LivingEntity) event.getObject()),
              () -> ModCapabilities.LIVING));
    } else if (event.getObject() instanceof ServerPlayerEntity) {
      ServerPlayer player = new ServerPlayer((ServerPlayerEntity) event.getObject());
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "living"),
              new SerializableCapabilityProvider<>(player, () -> ModCapabilities.LIVING));
    }
  }

  @SubscribeEvent
  public void handleAttachCapabilitiesItemStack(AttachCapabilitiesEvent<ItemStack> event) {
    final Item item = event.getObject().getItem();
    int hydration = -1;
    if (item == Items.APPLE || item == Items.RABBIT_STEW) {
      hydration = 2;
    } else if (item == Items.CARROT || item == Items.BEETROOT || item == Items.HONEY_BOTTLE) {
      hydration = 1;
    } else if (item == Items.CHORUS_FRUIT || item == Items.SWEET_BERRIES) {
      hydration = 3;
    } else if (item == Items.ENCHANTED_GOLDEN_APPLE || item == Items.GOLDEN_APPLE
        || item == Items.MUSHROOM_STEW || item == Items.SUSPICIOUS_STEW
        || item == Items.BEETROOT_SOUP || item == Items.MELON_SLICE) {
      hydration = 5;
    } else if (item == Items.GOLDEN_CARROT) {
      hydration = 6;
    }
    if (hydration != -1) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
              new SimpleCapabilityProvider<>(new PresetHydration(hydration),
                  () -> ModCapabilities.HYDRATION));
    } else if (item == Items.POTION) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
              new SimpleCapabilityProvider<>(new DefaultHydration(),
                  () -> ModCapabilities.HYDRATION));
    }
  }

  @SubscribeEvent
  public void handleUseItem(LivingEntityUseItemEvent.Finish event) {
    event
        .getItem()
        .getCapability(ModCapabilities.HYDRATION)
        .map(hydration -> hydration.getHydration(event.getItem()))
        .ifPresent(hydration -> event
            .getEntityLiving()
            .addPotionEffect(new EffectInstance(ModEffects.HYDRATE.get(), 1, hydration)));
  }
}
