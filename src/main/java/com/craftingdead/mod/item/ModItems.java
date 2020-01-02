package com.craftingdead.mod.item;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.animation.IGunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.potion.ModEffects;
import com.craftingdead.mod.type.Backpack;
import com.craftingdead.mod.util.ModDamageSource;
import com.craftingdead.mod.util.ModSoundEvents;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

  public static final DeferredRegister<Item> ITEMS =
      new DeferredRegister<>(ForgeRegistries.ITEMS, CraftingDead.ID);

  // ================================================================================
  // Guns
  // ================================================================================

  public static final RegistryObject<Item> ACR = ITEMS
      .register("acr",
          () -> new GunItem((GunItem.Properties) new GunItem.Properties()
              .setFireRate(80)
              .setDamage(7)
              .setReloadDurationTicks((int) (20 * 2.2F))
              .setAccuracy(0.8F)
              .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
              .setShootSound(ModSoundEvents.ACR_SHOOT)
              .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, RifleShootAnimation::new))));

  public static final RegistryObject<Item> AK47 = ITEMS
      .register("ak47", () -> new GunItem(new GunItem.Properties()
          .setFireRate(80)
          .setDamage(7)
          .setReloadDurationTicks(20 * 4)
          .setAccuracy(0.8F)
          .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
          .setShootSound(ModSoundEvents.AK47_SHOOT)
          .setReloadSound(ModSoundEvents.AK47_RELOAD)
          .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, RifleShootAnimation::new))
          .setAcceptedMagazines(
              ImmutableSet.of(new ResourceLocation(CraftingDead.ID, "ak47_30_round_magazine")))));

  public static final RegistryObject<Item> DESERT_EAGLE = ITEMS
      .register("desert_eagle", () -> new GunItem(new GunItem.Properties()
          .setFireRate(0)
          .setDamage(8)
          .setReloadDurationTicks(20 * 2)
          .setAccuracy(0.7F)
          .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
          .setShootSound(ModSoundEvents.DESERT_EAGLE_SHOOT)
          .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, PistolShootAnimation::new))));

  public static final RegistryObject<Item> M4A1 = ITEMS
      .register("m4a1",
          () -> new GunItem(new GunItem.Properties()
              .setFireRate(80)
              .setDamage(7)
              .setReloadDurationTicks(20 * 2)
              .setAccuracy(0.9F)
              .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
              .setShootSound(ModSoundEvents.M4A1_SHOOT)
              .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, RifleShootAnimation::new))));

  public static final RegistryObject<Item> M9 = ITEMS
      .register("m9", () -> new GunItem(new GunItem.Properties()
          .setFireRate(80)
          .setDamage(7)
          .setReloadDurationTicks((int) (20 * 1.5F))
          .setAccuracy(0.9F)
          .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
          .setShootSound(ModSoundEvents.M9_SHOOT)
          .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, PistolShootAnimation::new))));

  public static final RegistryObject<Item> TASER = ITEMS
      .register("taser", () -> new GunItem(new GunItem.Properties()
          .setFireRate(2000)
          .setDamage(7)
          .setReloadDurationTicks(20 * 3)
          .setAccuracy(0.9F)
          .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
          .setShootSound(ModSoundEvents.TASER_SHOOT)
          .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, PistolShootAnimation::new))));

  public static final RegistryObject<Item> MAGNUM = ITEMS
      .register("magnum", () -> new GunItem(new GunItem.Properties()
          .setFireRate(80)
          .setDamage(7)
          .setReloadDurationTicks(20 * 2)
          .setAccuracy(0.9F)
          .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
          .setShootSound(ModSoundEvents.MAGNUM_SHOOT)
          .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, PistolShootAnimation::new))));

  public static final RegistryObject<Item> FN57 = ITEMS
      .register("fn57", () -> new GunItem(new GunItem.Properties()
          .setFireRate(80)
          .setDamage(7)
          .setReloadDurationTicks((int) (20 * 1.5F))
          .setAccuracy(0.9F)
          .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
          .setShootSound(ModSoundEvents.FN57_SHOOT)
          .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, PistolShootAnimation::new))));

  public static final RegistryObject<Item> AK47_30_ROUND_MAGAZINE = ITEMS
      .register("ak47_30_round_magazine",
          () -> new MagazineItem(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_WEAPON),
              new Magazine(45, 30)));

  // ================================================================================
  // Tools
  // ================================================================================

  public static final RegistryObject<Item> CAN_OPENER = ITEMS
      .register("can_opener", () -> new ToolItem(
          new Item.Properties().maxDamage(8).group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> SCREWDRIVER = ITEMS
      .register("screwdriver", () -> new ToolItem(
          new Item.Properties().maxDamage(4).group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> MULTI_TOOL = ITEMS
      .register("multi_tool", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxDamage(20).group(ModItemGroups.CRAFTING_DEAD_MISC)));

  // ================================================================================
  // Consumable
  // ================================================================================

  public static final RegistryObject<Item> EMPTY_WATER_BOTTLE = ITEMS
      .register("empty_water_bottle",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "bottled_water"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> BOTTLED_WATER = ITEMS
      .register("bottled_water",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 7), 1.0F)
              .containerItem(EMPTY_WATER_BOTTLE.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> POWER_BAR = ITEMS
      .register("power_bar",
          () -> new Item(new Item.Properties()
              .food(ModFoods.POWER_BAR)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANDY_BAR = ITEMS
      .register("candy_bar",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANDY_BAR)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CEREAL = ITEMS
      .register("cereal",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CEREAL)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_CORN = ITEMS
      .register("canned_corn",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_CORN = ITEMS
      .register("open_canned_corn",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_CORN)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_BEANS = ITEMS
      .register("canned_beans",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_BEANS = ITEMS
      .register("open_canned_beans",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_BEANS)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_TUNA = ITEMS
      .register("canned_tuna",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_TUNA = ITEMS
      .register("open_canned_tuna",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_TUNA)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_PEACH = ITEMS
      .register("canned_peach",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_PEACH = ITEMS
      .register("open_canned_peach",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_PEACH)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_PASTA = ITEMS
      .register("canned_pasta",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_PASTA = ITEMS
      .register("open_canned_pasta",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_PASTA)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_BACON = ITEMS
      .register("canned_bacon",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_BACON = ITEMS
      .register("open_canned_bacon",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_BACON)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_CUSTARD = ITEMS
      .register("canned_custard",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_CUSTARD = ITEMS
      .register("open_canned_custard",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_CUSTARD)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_PICKLES = ITEMS
      .register("canned_pickles",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_PICKLES = ITEMS
      .register("open_canned_pickles",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_PICKLES)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_DOG_FOOD = ITEMS
      .register("canned_dog_food",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_DOG_FOOD = ITEMS
      .register("open_canned_dog_food",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_DOG_FOOD)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CANNED_TOMATO_SOUP = ITEMS
      .register("canned_tomato_soup",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> OPEN_CANNED_TOMATO_SOUP = ITEMS
      .register("open_canned_tomato_soup",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CANNED_TOMATO_SOUP)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_ORANGE_SODA = ITEMS
      .register("empty_orange_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ORANGE_SODA = ITEMS
      .register("orange_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 3), 1.0F)
              .containerItem(EMPTY_ORANGE_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_ICED_TEA = ITEMS
      .register("empty_iced_tea",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ICED_TEA = ITEMS
      .register("iced_tea",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 3), 1.0F)
              .containerItem(EMPTY_ICED_TEA.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_JUICE_POUCH = ITEMS
      .register("empty_juice_pouch",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> JUICE_POUCH = ITEMS
      .register("juice_pouch",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 1), 1.0F)
              .containerItem(EMPTY_JUICE_POUCH.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_PEPE_SODA = ITEMS
      .register("empty_pepe_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> PEPE_SODA = ITEMS
      .register("pepe_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 3), 1.0F)
              .containerItem(EMPTY_PEPE_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_LEMON_SODA = ITEMS
      .register("empty_lemon_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> LEMON_SODA = ITEMS
      .register("lemon_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 3), 1.0F)
              .containerItem(EMPTY_LEMON_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_COLA_SODA = ITEMS
      .register("empty_cola_soda",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> COLA_SODA = ITEMS
      .register("cola_soda",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 3), 1.0F)
              .containerItem(EMPTY_COLA_SODA.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_MILK_CARTON = ITEMS
      .register("empty_milk_carton",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> MILK_CARTON = ITEMS
      .register("milk_carton",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 7), 1.0F)
              .containerItem(EMPTY_MILK_CARTON.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ROTTON_MILK = ITEMS
      .register("rotten_milk",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 3), 1.0F)
              .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
              .containerItem(EMPTY_MILK_CARTON.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_COLA_POP = ITEMS
      .register("empty_cola_pop",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> COLA_POP = ITEMS
      .register("cola_pop",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 5), 1.0F)
              .containerItem(EMPTY_COLA_POP.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_IRON_BREW = ITEMS
      .register("empty_iron_brew",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> IRON_BREW = ITEMS
      .register("iron_brew",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 8), 1.0F)
              .containerItem(EMPTY_IRON_BREW.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_SPRITE = ITEMS
      .register("empty_sprite",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> SPRITE = ITEMS
      .register("sprite",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 8), 1.0F)
              .containerItem(EMPTY_SPRITE.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_ZOMBIE_ENERGY = ITEMS
      .register("empty_zombie_energy",
          () -> new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ZOMBIE_ENERGY = ITEMS
      .register("zombie_energy",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 8), 1.0F)
              .containerItem(EMPTY_ZOMBIE_ENERGY.get())
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> MRE = ITEMS
      .register("mre", () -> new Item(
          new Item.Properties().food(ModFoods.MRE).group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ORANGE = ITEMS
      .register("orange",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ORANGE)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ROTTEN_ORANGE = ITEMS
      .register("rotten_orange",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_ORANGE)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> PEAR = ITEMS
      .register("pear",
          () -> new Item(new Item.Properties()
              .food(ModFoods.PEAR)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ROTTEN_PEAR = ITEMS
      .register("rotten_pear",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_PEAR)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> RICE_BAG = ITEMS
      .register("rice_bag",
          () -> new Item(new Item.Properties()
              .food(ModFoods.RICE_BAG)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> APPLE = ITEMS
      .register("apple",
          () -> new Item(new Item.Properties()
              .food(ModFoods.APPLE)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ROTTEN_APPLE = ITEMS
      .register("rotten_apple",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_APPLE)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> NOODLES = ITEMS
      .register("noodles",
          () -> new Item(new Item.Properties()
              .food(ModFoods.NOODLE_CUP)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> WATERMELON = ITEMS
      .register("watermelon",
          () -> new Item(new Item.Properties()
              .food(ModFoods.WATERMELON)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ROTTEN_WATERMELON = ITEMS
      .register("rotten_watermelon",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_WATERMELON)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> BLUEBERRY = ITEMS
      .register("blueberry",
          () -> new Item(new Item.Properties()
              .food(ModFoods.BLUEBERRY)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ROTTEN_BLUEBERRY = ITEMS
      .register("rotten_blueberry",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_BLUEBERRY)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> RASPBERRY = ITEMS
      .register("raspberry",
          () -> new Item(new Item.Properties()
              .food(ModFoods.RASPBERRY)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> ROTTEN_RASPBERRY = ITEMS
      .register("rotten_raspberry",
          () -> new Item(new Item.Properties()
              .food(ModFoods.ROTTEN_RASPBERRY)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CHIPS = ITEMS
      .register("chips",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CHIPS)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> RANCH_CHIPS = ITEMS
      .register("ranch_chips",
          () -> new Item(new Item.Properties()
              .food(ModFoods.RANCH_CHIPS)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> CHEESE_CHIPS = ITEMS
      .register("cheese_chips",
          () -> new Item(new Item.Properties()
              .food(ModFoods.CHEESE_CHIPS)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> SALT_CHIPS = ITEMS
      .register("salt_chips",
          () -> new Item(new Item.Properties()
              .food(ModFoods.SALT_CHIPS)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> POPCORN = ITEMS
      .register("popcorn",
          () -> new Item(new Item.Properties()
              .food(ModFoods.POPCORN)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> NUTTY_CEREAL = ITEMS
      .register("nutty_cereal",
          () -> new Item(new Item.Properties()
              .food(ModFoods.NUTTY_CEREAL)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMERALD_CEREAL = ITEMS
      .register("emerald_cereal",
          () -> new Item(new Item.Properties()
              .food(ModFoods.EMERALD_CEREAL)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> FLAKE_CEREAL = ITEMS
      .register("flake_cereal",
          () -> new Item(new Item.Properties()
              .food(ModFoods.FLAKE_CEREAL)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_WATER_CANTEEN = ITEMS
      .register("empty_water_canteen",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "water_canteen"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> WATER_CANTEEN = ITEMS
      .register("water_canteen",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 8), 1.0F)
              .containerItem(EMPTY_WATER_CANTEEN.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> EMPTY_FLASK = ITEMS
      .register("empty_flask",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "flask"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> FLASK = ITEMS
      .register("flask",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 5), 1.0F)
              .containerItem(EMPTY_FLASK.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> TEA_FLASK = ITEMS
      .register("tea_flask",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 5), 1.0F)
              .containerItem(EMPTY_FLASK.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  public static final RegistryObject<Item> COFFEE_FLASK = ITEMS
      .register("coffee_flask",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .setUseAction(UseAction.DRINK)
              .effect(() -> new EffectInstance(ModEffects.hydrate, 1, 5), 1.0F)
              .containerItem(EMPTY_FLASK.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

  // ================================================================================
  // Medical
  // ================================================================================

  public static final RegistryObject<Item> EMPTY_BLOOD_BAG = ITEMS
      .register("empty_blood_bag",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "blood_bag"))
              .setEntityPredicate((entity) -> {
                if (entity instanceof PlayerEntity && ((PlayerEntity) entity).getHealth() > 4) {
                  entity.attackEntityFrom(ModDamageSource.BLEEDING, 2.0F);
                  return true;
                }
                return false;
              })
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BLOOD_BAG = ITEMS
      .register("blood_bag",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
              .maxStackSize(1)
              .containerItem(EMPTY_BLOOD_BAG.get())
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> FIRST_AID_KIT = ITEMS
      .register("first_aid_kit",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 1), 1.0F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> ADRENALINE_SYRINGE = ITEMS
      .register("adrenaline_syringe",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.SPEED, (20 * 20), 1), 1.0F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> SYRINGE = ITEMS
      .register("syringe",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "rbi_syringe"))
              .setEntityPredicate((entity) -> entity instanceof ZombieEntity)
              .setProbability(0.25F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> MORPHINE_SYRINGE = ITEMS
      .register("morphine_syringe",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BANDAGE = ITEMS
      .register("bandage",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> RBI_SYRINGE = ITEMS
      .register("rbi_syringe", () -> new Item(
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BOTTLED_RBI = ITEMS
      .register("bottled_rbi", () -> new Item(
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> DIRTY_RAG = ITEMS
      .register("dirty_rag",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "clean_rag"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BLOODY_RAG = ITEMS
      .register("bloody_rag",
          () -> new FillableItem((FillableItem.Properties) new FillableItem.Properties()
              .setFullItem(new ResourceLocation(CraftingDead.ID, "clean_rag"))
              .setBlockPredicate(
                  (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> CLEAN_RAG = ITEMS
      .register("clean_rag",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .containerItem(BLOODY_RAG.get())
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> SPLINT = ITEMS
      .register("splint",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> CURE_SYRINGE = ITEMS
      .register("cure_syringe",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> BOTTLED_CURE = ITEMS
      .register("bottled_cure", () -> new Item(
          new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

  public static final RegistryObject<Item> ANTIBIOTICS = ITEMS
      .register("antibiotics",
          () -> new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
              .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_MED)));

  // ================================================================================
  // Weapon
  // ================================================================================

  public static final RegistryObject<Item> CROWBAR = ITEMS
      .register("crowbar", () -> new MeleeWeaponItem(3, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> BAT = ITEMS
      .register("bat", () -> new MeleeWeaponItem(5, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> KATANA = ITEMS
      .register("katana", () -> new MeleeWeaponItem(18, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> PIPE = ITEMS
      .register("pipe", () -> new MeleeWeaponItem(9, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> RUSTY_PIPE = ITEMS
      .register("rusty_pipe", () -> new MeleeWeaponItem(9, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> FIRE_AXE = ITEMS
      .register("fire_axe", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> CHAINSAW = ITEMS
      .register("chainsaw", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> BOWIE = ITEMS
      .register("bowie", () -> new MeleeWeaponItem(15, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> GOLF_CLUB = ITEMS
      .register("golf_club", () -> new MeleeWeaponItem(6, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> NIGHT_STICK = ITEMS
      .register("night_stick", () -> new MeleeWeaponItem(4, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS
      .register("sledgehammer", () -> new MeleeWeaponItem(10, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> NAIL_BAT = ITEMS
      .register("nail_bat", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> SHOVEL = ITEMS
      .register("shovel", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> HATCHET = ITEMS
      .register("hatchet", () -> new MeleeWeaponItem(16, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> BROADSWORD = ITEMS
      .register("broadsword", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> MACHETE = ITEMS
      .register("machete", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> WEAPONIZED_SCYTHE = ITEMS
      .register("weaponized_scythe", () -> new MeleeWeaponItem(15, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> SCYTHE = ITEMS
      .register("scythe", () -> new MeleeWeaponItem(20, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> PICKAXE = ITEMS
      .register("pickaxe", () -> new MeleeWeaponItem(10, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> BO_STAFF = ITEMS
      .register("bo_staff", () -> new MeleeWeaponItem(4, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> WRENCH = ITEMS
      .register("wrench", () -> new MeleeWeaponItem(4, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> FRYING_PAN = ITEMS
      .register("frying_pan", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> BOLT_CUTTERS = ITEMS
      .register("bolt_cutters", () -> new MeleeWeaponItem(9, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> COMBAT_KNIFE = ITEMS
      .register("combat_knife", () -> new MeleeWeaponItem(14, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> STEEL_BAT = ITEMS
      .register("steel_bat", () -> new MeleeWeaponItem(7, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> CLEAVER = ITEMS
      .register("cleaver", () -> new MeleeWeaponItem(10, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  public static final RegistryObject<Item> BROKEN_BOTTLE = ITEMS
      .register("broken_bottle", () -> new MeleeWeaponItem(15, -2.4F,
          new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

  // ================================================================================
  // Backpack
  // ================================================================================

  public static final RegistryObject<Item> SMALL_RED_BACKPACK =
      ITEMS.register("small_red_backpack", () -> new BackpackItem(Backpack.SMALL));

  public static final RegistryObject<Item> SMALL_ORANGE_BACKPACK =
      ITEMS.register("small_orange_backpack", () -> new BackpackItem(Backpack.SMALL));

  public static final RegistryObject<Item> SMALL_YELLOW_BACKPACK =
      ITEMS.register("small_yellow_backpack", () -> new BackpackItem(Backpack.SMALL));

  public static final RegistryObject<Item> SMALL_GREEN_BACKPACK =
      ITEMS.register("small_green_backpack", () -> new BackpackItem(Backpack.SMALL));

  public static final RegistryObject<Item> SMALL_BLUE_BACKPACK =
      ITEMS.register("small_blue_backpack", () -> new BackpackItem(Backpack.SMALL));

  public static final RegistryObject<Item> SMALL_PURPLE_BACKPACK =
      ITEMS.register("small_purple_backpack", () -> new BackpackItem(Backpack.SMALL));

  public static final RegistryObject<Item> MEDIUM_RED_BACKPACK =
      ITEMS.register("medium_red_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_ORANGE_BACKPACK =
      ITEMS.register("medium_orange_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_YELLOW_BACKPACK =
      ITEMS.register("medium_yellow_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_GREEN_BACKPACK =
      ITEMS.register("medium_green_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_BLUE_BACKPACK =
      ITEMS.register("medium_blue_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_PURPLE_BACKPACK =
      ITEMS.register("medium_purple_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_GREY_BACKPACK =
      ITEMS.register("medium_grey_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_BLACK_BACKPACK =
      ITEMS.register("medium_black_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_GHILLIE_BACKPACK =
      ITEMS.register("medium_ghillie_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> MEDIUM_WHITE_BACKPACK =
      ITEMS.register("medium_white_backpack", () -> new BackpackItem(Backpack.MEDIUM));

  public static final RegistryObject<Item> LARGE_GREY_BACKPACK =
      ITEMS.register("large_grey_backpack", () -> new BackpackItem(Backpack.LARGE));

  public static final RegistryObject<Item> LARGE_GREEN_BACKPACK =
      ITEMS.register("large_green_backpack", () -> new BackpackItem(Backpack.LARGE));

  public static final RegistryObject<Item> LARGE_TAN_BACKPACK =
      ITEMS.register("large_tan_backpack", () -> new BackpackItem(Backpack.LARGE));

  public static final RegistryObject<Item> LARGE_BLACK_BACKPACK =
      ITEMS.register("large_black_backpack", () -> new BackpackItem(Backpack.LARGE));

  public static final RegistryObject<Item> LARGE_GHILLIE_BACKPACK =
      ITEMS.register("large_ghillie_backpack", () -> new BackpackItem(Backpack.LARGE));

  public static final RegistryObject<Item> GUN_BAG_BACKPACK =
      ITEMS.register("gun_bag_backpack", () -> new BackpackItem(Backpack.LARGE));

  public static final RegistryObject<Item> GREY_GUN_BAG_BACKPACK =
      ITEMS.register("grey_gun_bag_backpack", () -> new BackpackItem(Backpack.LARGE));

  public static final RegistryObject<Item> AMMO_BACKPACK =
      ITEMS.register("ammo_backpack", () -> new BackpackItem(Backpack.LARGE));

  // ================================================================================
  // Clothing
  // ================================================================================

  public static final RegistryObject<Item> ARMY_CLOTHING = ITEMS
      .register("army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SAS_CLOTHING = ITEMS
      .register("sas_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SPETSNAZ_CLOTHING = ITEMS
      .register("spetsnaz_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> POLICE_CLOTHING = ITEMS
      .register("police_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> CAMO_CLOTHING = ITEMS
      .register("camo_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> COMBAT_BDU_CLOTHING = ITEMS
      .register("combat_bdu_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> WINTER_ARMY_CLOTHING = ITEMS
      .register("winter_army_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> ARMY_DESERT_CLOTHING = ITEMS
      .register("army_desert_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> PILOT_CLOTHING = ITEMS
      .register("pilot_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> HAZMAT_CLOTHING = ITEMS
      .register("hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> TAC_GHILLIE_CLOTHING = ITEMS
      .register("tac_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SWAT_CLOTHING = ITEMS
      .register("swat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SPACE_SUIT_CLOTHING = ITEMS
      .register("space_suit_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SHERIFF_CLOTHING = ITEMS
      .register("sheriff_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> JUGGERNAUT_CLOTHING = ITEMS
      .register("juggernaut_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> FIREMAN_CLOTHING = ITEMS
      .register("fireman_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> DOCTOR_CLOTHING = ITEMS
      .register("doctor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SMART_CLOTHING = ITEMS
      .register("smart_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> CASUAL_GREEN_CLOTHING = ITEMS
      .register("casual_green_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> BUILDER_CLOTHING = ITEMS
      .register("builder_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> BUSINESS_CLOTHING = ITEMS
      .register("business_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SEC_GUARD_CLOTHING = ITEMS
      .register("sec_guard_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> MIL_HAZMAT_CLOTHING = ITEMS
      .register("mil_hazmat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> FULL_GHILLIE_CLOTHING = ITEMS
      .register("full_ghillie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> RED_DUSK_CLOTHING = ITEMS
      .register("red_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> CLONE_CLOTHING = ITEMS
      .register("clone_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> COOKIE_CLOTHING = ITEMS
      .register("cookie_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> DEADPOOL_CLOTHING = ITEMS
      .register("deadpool_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> NINJA_CLOTHING = ITEMS
      .register("ninja_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> ARMY_MEDIC_CLOTHING = ITEMS
      .register("army_medic_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> BLUE_DUSK_CLOTHING = ITEMS
      .register("blue_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> PRESIDENT_CLOTHING = ITEMS
      .register("president_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(0)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> YELLOW_DUSK_CLOTHING = ITEMS
      .register("yellow_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> ORANGE_DUSK_CLOTHING = ITEMS
      .register("orange_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> GREEN_DUSK_CLOTHING = ITEMS
      .register("green_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> WHITE_DUSK_CLOTHING = ITEMS
      .register("white_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> PURPLE_DUSK_CLOTHING = ITEMS
      .register("purple_dusk_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> SCUBA_CLOTHING = ITEMS
      .register("scuba_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(1)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> DDPAT_CLOTHING = ITEMS
      .register("ddpat_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(2)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  public static final RegistryObject<Item> CONTRACTOR_CLOTHING = ITEMS
      .register("contractor_clothing",
          () -> new ClothingItem((ClothingItem.Properties) new ClothingItem.Properties()
              .setArmorLevel(3)
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_WEARABLE)));

  // ================================================================================
  // Air Drop Radio
  // ================================================================================

  public static final RegistryObject<Item> MEDICAL_DROP_RADIO = ITEMS
      .register("medical_drop_radio",
          () -> new AirDropRadioItem((AirDropRadioItem.Properties) new AirDropRadioItem.Properties()
              .setLootTable(new ResourceLocation(CraftingDead.ID, "supply_drops/medical"))
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_GENERAL)));

  public static final RegistryObject<Item> MILITARY_DROP_RADIO = ITEMS
      .register("military_drop_radio",
          () -> new AirDropRadioItem((AirDropRadioItem.Properties) new AirDropRadioItem.Properties()
              .setLootTable(new ResourceLocation(CraftingDead.ID, "supply_drops/military"))
              .maxStackSize(1)
              .group(ModItemGroups.CRAFTING_DEAD_GENERAL)));

  // ================================================================================
  // Spawn Eggs
  // ================================================================================

  public static final RegistryObject<Item> ADVANCED_ZOMBIE_SPAWN_EGG = ITEMS
      .register("advanced_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.advancedZombie,
          0x000000, 0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> FAST_ZOMBIE_SPAWN_EGG = ITEMS
      .register("fast_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.fastZombie, 0x000000,
          0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> TANK_ZOMBIE_SPAWN_EGG = ITEMS
      .register("tank_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.tankZombie, 0x000000,
          0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

  public static final RegistryObject<Item> WEAK_ZOMBIE_SPAWN_EGG = ITEMS
      .register("weak_zombie_spawn_egg", () -> new SpawnEggItem(ModEntityTypes.weakZombie, 0x000000,
          0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));
}
