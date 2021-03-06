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

package com.craftingdead.core;

import java.util.ListIterator;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.core.action.ActionTypes;
import com.craftingdead.core.ammoprovider.AmmoProviderTypes;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.hydration.DefaultHydration;
import com.craftingdead.core.capability.hydration.PresetHydration;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.IPlayer;
import com.craftingdead.core.capability.living.LivingImpl;
import com.craftingdead.core.capability.living.PlayerImpl;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.command.Commands;
import com.craftingdead.core.data.ModItemTagsProvider;
import com.craftingdead.core.data.ModLootTableProvider;
import com.craftingdead.core.data.ModRecipeProvider;
import com.craftingdead.core.enchantment.ModEnchantments;
import com.craftingdead.core.entity.ModEntityTypes;
import com.craftingdead.core.entity.monster.AdvancedZombieEntity;
import com.craftingdead.core.entity.monster.DoctorZombieEntity;
import com.craftingdead.core.entity.monster.FastZombieEntity;
import com.craftingdead.core.entity.monster.GiantZombieEntity;
import com.craftingdead.core.entity.monster.PoliceZombieEntity;
import com.craftingdead.core.entity.monster.TankZombieEntity;
import com.craftingdead.core.entity.monster.WeakZombieEntity;
import com.craftingdead.core.event.CombatPickupEvent;
import com.craftingdead.core.inventory.CombatSlotType;
import com.craftingdead.core.inventory.container.ModContainerTypes;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.item.crafting.ModRecipeSerializers;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.SyncLivingMessage;
import com.craftingdead.core.particle.ModParticleTypes;
import com.craftingdead.core.potion.ModEffects;
import com.craftingdead.core.server.ServerDist;
import com.craftingdead.core.util.ArbitraryTooltips;
import com.craftingdead.core.util.ModSoundEvents;
import io.netty.buffer.Unpooled;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
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
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(CraftingDead.ID)
public class CraftingDead {

  public static final String ID = "craftingdead";

  public static final String VERSION;

  public static final CommonConfig commonConfig;
  public static final ForgeConfigSpec commonConfigSpec;

  static {
    VERSION = JarVersionLookupHandler
        .getImplementationVersion(CraftingDead.class)
        .orElse("[version]");

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

  public CraftingDead() {
    instance = this;

    this.modDist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleGatherData);
    modEventBus.addListener(this::handleEntityAttributeCreation);

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

    AmmoProviderTypes.AMMO_PROVIDER_TYPES.makeRegistry("ammo_provider_types", RegistryBuilder::new);
    AmmoProviderTypes.AMMO_PROVIDER_TYPES.register(modEventBus);

    // Should be registered after ITEMS registration
    modEventBus.addGenericListener(Item.class, ArbitraryTooltips::registerAll);

    MinecraftForge.EVENT_BUS.register(this);

    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfigSpec);
  }

  public IModDist getModDist() {
    return this.modDist;
  }

  public ClientDist getClientDist() {
    if (this.modDist instanceof ClientDist) {
      return (ClientDist) this.modDist;
    }
    throw new IllegalStateException("Accessing client dist on wrong side");
  }

  public boolean isTravelersBackpacksLoaded() {
    return this.travelersBackpacksLoaded;
  }

  public static CraftingDead getInstance() {
    return instance;
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    logger.info("Starting Crafting Dead, version {}", VERSION);
    NetworkChannel.loadChannels();
    event.enqueueWork(() -> {
      BrewingRecipeRegistry.addRecipe(Ingredient.of(ModItems.SYRINGE.get()),
          Ingredient.of(Tags.Items.DUSTS_REDSTONE),
          new ItemStack(ModItems.ADRENALINE_SYRINGE.get()));
      BrewingRecipeRegistry.addRecipe(Ingredient.of(ModItems.SYRINGE.get()),
          Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE),
          new ItemStack(ModItems.CURE_SYRINGE.get()));
    });

    this.travelersBackpacksLoaded = ModList.get().isLoaded(TRAVELERS_BACKPACK_ID);
    if (this.travelersBackpacksLoaded) {
      logger.info("Adding integration for " + TRAVELERS_BACKPACK_ID);
    }
  }

  private void handleGatherData(GatherDataEvent event) {
    DataGenerator dataGenerator = event.getGenerator();
    if (event.includeServer()) {
      dataGenerator.addProvider(new ModItemTagsProvider(dataGenerator,
          new ForgeBlockTagsProvider(dataGenerator, event.getExistingFileHelper()),
          event.getExistingFileHelper()));
      dataGenerator.addProvider(new ModRecipeProvider(dataGenerator));
      dataGenerator.addProvider(new ModLootTableProvider(dataGenerator));
    }
  }

  private void handleEntityAttributeCreation(EntityAttributeCreationEvent event) {
    event.put(ModEntityTypes.advancedZombie, AdvancedZombieEntity.registerAttributes().build());
    event.put(ModEntityTypes.doctorZombie, DoctorZombieEntity.registerAttributes().build());
    event.put(ModEntityTypes.fastZombie, FastZombieEntity.registerAttributes().build());
    event.put(ModEntityTypes.giantZombie, GiantZombieEntity.registerAttributes().build());
    event.put(ModEntityTypes.policeZombie, PoliceZombieEntity.registerAttributes().build());
    event.put(ModEntityTypes.tankZombie, TankZombieEntity.registerAttributes().build());
    event.put(ModEntityTypes.weakZombie, WeakZombieEntity.registerAttributes().build());
  }

  // ================================================================================
  // Common Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleEntityItemPickup(EntityItemPickupEvent event) {
    event.getPlayer().getCapability(ModCapabilities.LIVING).<IPlayer<?>>cast()
        .filter(IPlayer::isCombatModeEnabled).ifPresent(living -> {
          final ItemStack itemStack = event.getItem().getItem();
          CombatSlotType combatSlotType = CombatSlotType.getSlotType(itemStack).orElse(null);
          CombatPickupEvent combatPickupEvent = new CombatPickupEvent(itemStack, combatSlotType);
          if (MinecraftForge.EVENT_BUS.post(combatPickupEvent)) {
            event.setCanceled(true);
          } else if (combatSlotType != null) {
            if (combatSlotType.addToInventory(itemStack, event.getPlayer().inventory, false)) {
              // Allows normal processing of item pickup but prevents item being added to inventory
              // because we've already added it.
              event.setResult(Event.Result.ALLOW);
            } else {
              event.setCanceled(true);
            }
          }
        });
  }

  @SubscribeEvent
  public void handleBiomeLoading(BiomeLoadingEvent event) {
    ListIterator<MobSpawnInfo.Spawners> iterator =
        event.getSpawns().getSpawner(EntityClassification.MONSTER).listIterator();
    while (iterator.hasNext()) {
      MobSpawnInfo.Spawners spawnEntry = iterator.next();
      if (spawnEntry.type == EntityType.ZOMBIE) {
        iterator.add(new MobSpawnInfo.Spawners(ModEntityTypes.advancedZombie,
            spawnEntry.weight * 3, 2, 8));
        iterator.add(
            new MobSpawnInfo.Spawners(ModEntityTypes.fastZombie, spawnEntry.weight / 2, 2, 4));
        iterator
            .add(new MobSpawnInfo.Spawners(ModEntityTypes.tankZombie, spawnEntry.weight / 2, 2,
                4));
        iterator.add(
            new MobSpawnInfo.Spawners(ModEntityTypes.advancedZombie, spawnEntry.weight, 3, 8));
        iterator.add(
            new MobSpawnInfo.Spawners(ModEntityTypes.weakZombie, spawnEntry.weight, 3, 12));
      }
    }
  }

  @SubscribeEvent
  public void handleRegisterCommands(RegisterCommandsEvent event) {
    Commands.register(event.getDispatcher());
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingSetTarget(LivingSetAttackTargetEvent event) {
    if (event.getTarget() != null && event.getEntityLiving() instanceof MobEntity) {
      MobEntity mobEntity = (MobEntity) event.getEntityLiving();
      if (mobEntity.hasEffect(ModEffects.FLASH_BLINDNESS.get())) {
        mobEntity.setTarget(null);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDeath(LivingDeathEvent event) {
    if (event.getEntity()
        .getCapability(ModCapabilities.LIVING)
        .map(living -> living.onDeath(event.getSource()))
        .orElse(false)
        || (event.getSource().getEntity() != null && event
            .getSource()
            .getEntity()
            .getCapability(ModCapabilities.LIVING)
            .map(living -> living.onKill(event.getEntity()))
            .orElse(false))) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDrops(LivingDropsEvent event) {
    event.getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onDeathDrops(event.getSource(), event.getDrops())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingAttack(LivingAttackEvent event) {
    event.getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setCanceled(living.onAttacked(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void handleLivingDamage(LivingDamageEvent event) {
    event.getEntity()
        .getCapability(ModCapabilities.LIVING)
        .ifPresent(
            living -> event.setAmount(living.onDamaged(event.getSource(), event.getAmount())));
  }

  @SubscribeEvent
  public void handlePlayerClone(PlayerEvent.Clone event) {
    IPlayer.getExpected(event.getPlayer()).copyFrom(IPlayer.getExpected(event.getOriginal()),
        event.isWasDeath());
  }

  @SubscribeEvent
  public void handleUseItem(LivingEntityUseItemEvent.Finish event) {
    event.getItem()
        .getCapability(ModCapabilities.HYDRATION)
        .map(hydration -> hydration.getHydration(event.getItem()))
        .ifPresent(hydration -> event
            .getEntityLiving()
            .addEffect(new EffectInstance(ModEffects.HYDRATE.get(), 1, hydration)));
  }

  @SubscribeEvent
  public void handleLivingUpdate(LivingUpdateEvent event) {
    event.getEntityLiving().getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      living.tick();
      if (!living.getEntity().getCommandSenderWorld().isClientSide() && living.requiresSync()) {
        PacketBuffer data = new PacketBuffer(Unpooled.buffer());
        living.encode(data, false);
        NetworkChannel.PLAY.getSimpleChannel().send(
            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(living::getEntity),
            new SyncLivingMessage(living.getEntity().getId(), data));
      }
    });
  }

  @SubscribeEvent
  public void handlePlayerTick(TickEvent.PlayerTickEvent event) {
    switch (event.phase) {
      case END:
        event.player.getCapability(ModCapabilities.LIVING)
            .filter(living -> living instanceof IPlayer)
            .map(living -> (IPlayer<?>) living)
            .ifPresent(IPlayer::playerTick);
        break;
      default:
        break;
    }
  }

  @SubscribeEvent
  public void handleAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof LivingEntity) {
      ILiving<?, ?> living = event.getObject() instanceof PlayerEntity
          ? new PlayerImpl<>((PlayerEntity) event.getObject())
          : new LivingImpl<>((LivingEntity) event.getObject());
      event.addCapability(ILiving.CAPABILITY_KEY, new SerializableCapabilityProvider<>(
          living, () -> ModCapabilities.LIVING));
      living.load();
    }
  }

  @SubscribeEvent
  public void handleAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
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
      event.addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
          new SimpleCapabilityProvider<>(new PresetHydration(hydration),
              () -> ModCapabilities.HYDRATION));
    } else if (item == Items.POTION) {
      event.addCapability(new ResourceLocation(CraftingDead.ID, "hydration"),
          new SimpleCapabilityProvider<>(new DefaultHydration(),
              () -> ModCapabilities.HYDRATION));
    }
  }

  @SubscribeEvent
  public void handlePlayerStartTracking(PlayerEvent.StartTracking event) {
    event.getTarget().getCapability(ModCapabilities.LIVING)
        .ifPresent(living -> {
          living.onStartTracking((ServerPlayerEntity) event.getPlayer());
          PacketBuffer data = new PacketBuffer(Unpooled.buffer());
          living.encode(data, true);
          NetworkChannel.PLAY.getSimpleChannel().send(
              PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
              new SyncLivingMessage(living.getEntity().getId(), data));
        });
  }
}
