package com.craftingdead.mod;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.living.DefaultLiving;
import com.craftingdead.mod.capability.living.ILiving;
import com.craftingdead.mod.capability.living.player.ServerPlayer;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.enchantment.ModEnchantments;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.entity.monster.AdvancedZombieEntity;
import com.craftingdead.mod.inventory.container.ModContainerTypes;
import com.craftingdead.mod.item.ModItems;
import com.craftingdead.mod.network.NetworkChannel;
import com.craftingdead.mod.potion.ModEffects;
import com.craftingdead.mod.server.ServerDist;
import com.craftingdead.mod.util.ModSoundEvents;
import com.craftingdead.mod.world.biome.ModBiomes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.JarVersionLookupHandler;

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

  public CraftingDead() {
    instance = this;

    this.modDist = DistExecutor.runForDist(() -> ClientDist::new, () -> ServerDist::new);

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.register(this);

    ModEntityTypes.initialize();
    modEventBus.addGenericListener(EntityType.class, ModEntityTypes::register);

    ModBiomes.BIOMES.register(modEventBus);
    ModItems.ITEMS.register(modEventBus);
    ModSoundEvents.SOUND_EVENTS.register(modEventBus);
    ModContainerTypes.CONTAINERS.register(modEventBus);
    ModEffects.EFFECTS.register(modEventBus);
    ModEnchantments.ENCHANTMENTS.register(modEventBus);

    MinecraftForge.EVENT_BUS.register(this);

    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfigSpec);
  }

  public IModDist getModDist() {
    return this.modDist;
  }

  public static CraftingDead getInstance() {
    return instance;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  @SubscribeEvent
  public void handleCommonSetup(FMLCommonSetupEvent event) {
    logger.info("Starting {}, version {}", DISPLAY_NAME, VERSION);
    NetworkChannel.loadChannels();
    logger.info("Registering capabilities");
    ModCapabilities.registerCapabilities();
  }

  // ================================================================================
  // Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handlePlayerTick(LivingEvent.LivingUpdateEvent event) {
    event.getEntityLiving().getCapability(ModCapabilities.LIVING).ifPresent(ILiving::tick);
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
  public void handleLivingAttack(LivingAttackEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(living -> living.onAttacked(event.getSource(), event.getAmount()));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDamage(LivingDamageEvent event) {
    event
        .getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(living -> living.onDamaged(event.getSource(), event.getAmount()));
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
              .ifPresent(that -> {
                player.copyFrom(that, event.isWasDeath());
              });
        });
  }

  @SubscribeEvent
  public void handleAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof AdvancedZombieEntity) {
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "living"),
              new SerializableProvider<>(new DefaultLiving<>((LivingEntity) event.getObject()),
                  () -> ModCapabilities.LIVING));
    } else if (event.getObject() instanceof ServerPlayerEntity) {
      ServerPlayer player = new ServerPlayer((ServerPlayerEntity) event.getObject());
      event
          .addCapability(new ResourceLocation(CraftingDead.ID, "living"),
              new SerializableProvider<>(player, () -> ModCapabilities.LIVING));
    }
  }
}
