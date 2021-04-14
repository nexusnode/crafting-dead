package com.craftingdead.virus;

import java.util.ListIterator;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.event.GunEvent;
import com.craftingdead.core.inventory.InventorySlotType;
import com.craftingdead.core.item.ModItems;
import com.craftingdead.core.living.IPlayer;
import com.craftingdead.virus.action.VirusActionTypes;
import com.craftingdead.virus.client.ClientDist;
import com.craftingdead.virus.data.VirusItemTagsProvider;
import com.craftingdead.virus.enchantment.VirusEnchantments;
import com.craftingdead.virus.entity.ModEntityTypes;
import com.craftingdead.virus.entity.monster.AdvancedZombieEntity;
import com.craftingdead.virus.entity.monster.DoctorZombieEntity;
import com.craftingdead.virus.entity.monster.FastZombieEntity;
import com.craftingdead.virus.entity.monster.GiantZombieEntity;
import com.craftingdead.virus.entity.monster.PoliceZombieEntity;
import com.craftingdead.virus.entity.monster.TankZombieEntity;
import com.craftingdead.virus.entity.monster.WeakZombieEntity;
import com.craftingdead.virus.item.VirusItems;
import com.craftingdead.virus.potion.VirusEffects;
import com.craftingdead.virus.server.ServerDist;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CraftingDeadVirus.ID)
public class CraftingDeadVirus {

  public static final String ID = "craftingdeadvirus";

  /**
   * The % chance of getting infected by a zombie.
   */
  private static final float ZOMBIE_INFECTION_CHANCE = 0.1F;

  private static CraftingDeadVirus instance;

  private final IModDist modDist;

  public CraftingDeadVirus() {
    instance = this;

    this.modDist = DistExecutor.safeRunForDist(() -> ClientDist::new, () -> ServerDist::new);

    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::handleCommonSetup);
    modEventBus.addListener(this::handleEntityAttributeCreation);
    modEventBus.addListener(this::handleGatherData);

    MinecraftForge.EVENT_BUS.register(this);

    ModEntityTypes.initialize();
    modEventBus.addGenericListener(EntityType.class, ModEntityTypes::registerAll);

    VirusEnchantments.ENCHANTMENTS.register(modEventBus);
    VirusActionTypes.ACTION_TYPES.register(modEventBus);
    VirusItems.ITEMS.register(modEventBus);
    VirusEffects.EFFECTS.register(modEventBus);
  }

  public IModDist getModDist() {
    return this.modDist;
  }

  public CraftingDeadVirus getInstance() {
    return instance;
  }

  public static void infect(PlayerEntity playerEntity, float chance) {
    if (!playerEntity.isCreative()
        && playerEntity.getCommandSenderWorld().getDifficulty() != Difficulty.PEACEFUL
        && playerEntity.getRandom().nextFloat() < chance
        && !playerEntity.hasEffect(VirusEffects.INFECTION.get())) {
      playerEntity.displayClientMessage(new TranslationTextComponent("message.infected")
          .setStyle(Style.EMPTY.applyFormats(TextFormatting.RED).withBold(true)),
          true);
      playerEntity.addEffect(new EffectInstance(VirusEffects.INFECTION.get(), 9999999));
    }
  }

  // ================================================================================
  // Mod Events
  // ================================================================================

  private void handleCommonSetup(FMLCommonSetupEvent event) {
    BrewingRecipeRegistry.addRecipe(Ingredient.of(ModItems.SYRINGE.get()),
        Ingredient.of(Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE),
        new ItemStack(VirusItems.CURE_SYRINGE.get()));
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

  private void handleGatherData(GatherDataEvent event) {
    DataGenerator dataGenerator = event.getGenerator();
    if (event.includeServer()) {
      dataGenerator.addProvider(new VirusItemTagsProvider(dataGenerator,
          new ForgeBlockTagsProvider(dataGenerator, event.getExistingFileHelper()),
          event.getExistingFileHelper()));
    }
  }

  // ================================================================================
  // Common Forge Events
  // ================================================================================

  @SubscribeEvent
  public void handleGunHitEntity(GunEvent.HitEntity event) {
    event.getTarget().getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof IPlayer)
        .map(living -> (IPlayer<?>) living)
        .ifPresent(living -> {
          float clothingProtectionPct = living.getItemHandler()
              .getStackInSlot(InventorySlotType.CLOTHING.getIndex())
              .getCapability(ModCapabilities.CLOTHING)
              .map(clothing -> clothing.hasEnhancedProtection() ? 0.5F : 0.0F)
              .orElse(1.0F);

          float enchantmentPct =
              EnchantmentHelper.getItemEnchantmentLevel(VirusEnchantments.INFECTION.get(),
                  event.getItemStack()) / (float) VirusEnchantments.INFECTION.get().getMaxLevel();

          infect(living.getEntity(), enchantmentPct + clothingProtectionPct);
        });
  }

  @SubscribeEvent
  public void handleLivingAttack(LivingAttackEvent event) {
    LivingEntity entity = event.getEntityLiving();
    if (entity instanceof PlayerEntity && event.getSource().getEntity() instanceof ZombieEntity) {
      infect((PlayerEntity) entity, ZOMBIE_INFECTION_CHANCE);
    }
  }

  @SubscribeEvent
  public void handlePlayerTick(PlayerTickEvent event) {
    switch (event.phase) {
      case START:
        if ((event.player.isCreative() || event.player.level.getDifficulty() == Difficulty.PEACEFUL)
            && event.player.hasEffect(VirusEffects.INFECTION.get())) {
          event.player.removeEffect(VirusEffects.INFECTION.get());
        }
        break;
      default:
        break;
    }
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
}
