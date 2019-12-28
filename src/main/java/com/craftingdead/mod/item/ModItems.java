package com.craftingdead.mod.item;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.animation.IGunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.client.model.builtin.ItemRendererDispatcher;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

  public static final DeferredRegister<Item> ITEMS =
      new DeferredRegister<>(ForgeRegistries.ITEMS, CraftingDead.ID);

  private static final List<Item> toRegister = new ArrayList<>();

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
              .setAnimations(ImmutableMap.of(IGunAnimation.Type.SHOOT, RifleShootAnimation::new))
              .setTEISR(() -> () -> ItemRendererDispatcher.instance)));

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
              .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 7), 1.0F)
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

  public static Item orangeSoda;
  public static Item emptyOrangeSoda;
  public static Item icedTea;
  public static Item emptyIcedTea;
  public static Item juicePouch;
  public static Item emptyJuicePouch;
  public static Item pepeSoda;
  public static Item emptyPepeSoda;
  public static Item lemonSoda;
  public static Item emptyLemonSoda;
  public static Item colaSoda;
  public static Item emptyColaSoda;
  public static Item milkCarton;
  public static Item emptyMilkCarton;
  public static Item rottenMilk;
  public static Item colaPop;
  public static Item emptyColaPop;
  public static Item ironBrew;
  public static Item emptyIronBrew;
  public static Item sprite;
  public static Item emptySprite;
  public static Item zombieEnergy;
  public static Item emptyZombieEnergy;
  public static Item mre;
  public static Item orange;
  public static Item rottenOrange;
  public static Item pear;
  public static Item rottenPear;
  public static Item riceBag;
  public static Item apple;
  public static Item rottenApple;
  public static Item noodles;
  public static Item watermelon;
  public static Item rottenWatermelon;
  public static Item blueberry;
  public static Item rottenBlueberry;
  public static Item raspberry;
  public static Item rottenRaspberry;
  public static Item chips;
  public static Item ranchChips;
  public static Item cheeseChips;
  public static Item saltChips;
  public static Item popcorn;
  public static Item nuttyCereal;
  public static Item emeraldCereal;
  public static Item flakeCereal;
  public static Item emptyWaterCanteen;
  public static Item waterCanteen;
  public static Item flask;
  public static Item emptyFlask;
  public static Item teaFlask;
  public static Item coffeeFlask;

  // medic
  public static Item bloodBag;
  public static Item emptyBloodBag;
  public static Item firstAidKit;
  public static Item adrenalineSyringe;
  public static Item syringe;
  public static Item morphineSyringe;
  public static Item bandage;
  public static Item rbiSyringe;
  public static Item bottledRbi;
  public static Item dirtyRag;
  public static Item cleanRag;
  public static Item splint;
  public static Item cureSyringe;
  public static Item bottledCure;
  public static Item antibiotics;
  public static Item bloodyRag;

  // weapon
  public static Item crowbar;
  public static Item bat;
  public static Item katana;
  public static Item pipe;
  public static Item rustypipe;
  public static Item fireaxe;
  public static Item chainsaw;
  public static Item bowie;
  public static Item golfclub;
  public static Item nightstick;
  public static Item sledgehammer;
  public static Item nailBat;
  public static Item shovel;
  public static Item hatchet;
  public static Item broadsword;
  public static Item machete;
  public static Item scytheweapon;
  public static Item scythe;
  public static Item pickaxe;
  public static Item bostaff;
  public static Item wrench;
  public static Item fryingPan;
  public static Item boltCutters;
  public static Item combatKnife;
  public static Item steelBat;
  public static Item cleaver;
  public static Item brokenBottle;

  // backpack
  public static Item smallRedBackpack;
  public static Item smallOrangeBackpack;
  public static Item smallYellowBackpack;
  public static Item smallGreenBackpack;
  public static Item smallBlueBackpack;
  public static Item smallPurpleBackpack;
  public static Item mediumRedBackpack;
  public static Item mediumOrangeBackpack;
  public static Item mediumYellowBackpack;
  public static Item mediumGreenBackpack;
  public static Item mediumBlueBackpack;
  public static Item mediumPurpleBackpack;
  public static Item mediumGreyBackpack;
  public static Item mediumBlackBackpack;
  public static Item mediumGhillieBackpack;
  public static Item mediumWhiteBackpack;
  public static Item largeGreyBackpack;
  public static Item largeGreenBackpack;
  public static Item largeTanBackpack;
  public static Item largeBlackBackpack;
  public static Item largeGhillieBackpack;
  public static Item gunBagBackpack;
  public static Item greyGunBagBackpack;
  public static Item ammoBagBackpack;


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

  public static void initialize() {

    // ================================================================================
    // Food
    // ================================================================================

    mre = add("mre", new Item(
        new Item.Properties().food(ModFoods.MRE).group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    orange = add("orange",
        new Item(new Item.Properties()
            .food(ModFoods.ORANGE)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenOrange = add("rotten_orange",
        new Item(new Item.Properties()
            .food(ModFoods.ROTTEN_ORANGE)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    pear = add("pear", new Item(
        new Item.Properties().food(ModFoods.PEAR).group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenPear = add("rotten_pear",
        new Item(new Item.Properties()
            .food(ModFoods.ROTTEN_PEAR)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    apple = add("apple", new Item(
        new Item.Properties().food(ModFoods.APPLE).group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenApple = add("rotten_apple",
        new Item(new Item.Properties()
            .food(ModFoods.ROTTEN_APPLE)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    riceBag = add("rice_bag",
        new Item(new Item.Properties()
            .food(ModFoods.RICE_BAG)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    noodles = add("noodles",
        new Item(new Item.Properties()
            .food(ModFoods.NOODLE_CUP)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    watermelon = add("watermelon",
        new Item(new Item.Properties()
            .food(ModFoods.WATERMELON)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenWatermelon = add("rotten_watermelon",
        new Item(new Item.Properties()
            .food(ModFoods.ROTTEN_WATERMELON)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    blueberry = add("blueberry",
        new Item(new Item.Properties()
            .food(ModFoods.BLUEBERRY)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenBlueberry = add("rotten_blueberry",
        new Item(new Item.Properties()
            .food(ModFoods.ROTTEN_BLUEBERRY)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    raspberry = add("raspberry",
        new Item(new Item.Properties()
            .food(ModFoods.RASPBERRY)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenRaspberry = add("rotten_raspberry",
        new Item(new Item.Properties()
            .food(ModFoods.ROTTEN_RASPBERRY)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    chips = add("chips", new Item(
        new Item.Properties().food(ModFoods.CHIPS).group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    ranchChips = add("ranch_chips",
        new Item(new Item.Properties()
            .food(ModFoods.RANCH_CHIPS)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cheeseChips = add("cheese_chips",
        new Item(new Item.Properties()
            .food(ModFoods.CHEESE_CHIPS)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    saltChips = add("salt_chips",
        new Item(new Item.Properties()
            .food(ModFoods.SALT_CHIPS)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    popcorn = add("popcorn",
        new Item(new Item.Properties()
            .food(ModFoods.POPCORN)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    nuttyCereal = add("nutty_cereal",
        new Item(new Item.Properties()
            .food(ModFoods.NUTTY_CEREAL)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emeraldCereal = add("emerald_cereal",
        new Item(new Item.Properties()
            .food(ModFoods.EMERALD_CEREAL)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    flakeCereal = add("flake_cereal",
        new Item(new Item.Properties()
            .food(ModFoods.FLAKE_CEREAL)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    // ================================================================================
    // Drinks
    // ================================================================================

    emptyWaterCanteen = add("empty_water_canteen",
        new FillableItem((FillableItem.Properties) new FillableItem.Properties()
            .setFullItem(new ResourceLocation(CraftingDead.ID, "water_canteen"))
            .setBlockPredicate(
                (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    waterCanteen = add("water_canteen",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
            .containerItem(emptyWaterCanteen)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyFlask = add("empty_flask",
        new FillableItem((FillableItem.Properties) new FillableItem.Properties()
            .setFullItem(new ResourceLocation(CraftingDead.ID, "flask"))
            .setBlockPredicate(
                (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    flask = add("flask",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
            .containerItem(emptyFlask)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    teaFlask = add("tea_flask",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
            .containerItem(emptyFlask)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    coffeeFlask = add("coffee_flask",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
            .containerItem(emptyFlask)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyOrangeSoda = add("empty_orange_soda",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    orangeSoda = add("orange_soda",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
            .containerItem(emptyOrangeSoda)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyIcedTea = add("empty_iced_tea",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    icedTea = add("iced_tea",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
            .containerItem(emptyIcedTea)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyJuicePouch = add("empty_juice_pouch",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    juicePouch = add("juice_pouch",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 1), 1.0F)
            .containerItem(emptyJuicePouch)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyPepeSoda = add("empty_pepe_soda",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    pepeSoda = add("pepe_soda",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
            .containerItem(emptyPepeSoda)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyLemonSoda = add("empty_lemon_soda",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    lemonSoda = add("lemon_soda",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
            .containerItem(emptyLemonSoda)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyColaSoda = add("empty_cola_soda",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    colaSoda = add("cola_soda",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
            .containerItem(emptyColaSoda)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyMilkCarton = add("empty_milk_carton",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    milkCarton = add("milk_carton",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 7), 1.0F)
            .containerItem(emptyMilkCarton)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenMilk = add("rotten_milk",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 3), 1.0F)
            .effect(() -> new EffectInstance(Effects.HUNGER, 600, 1), 0.2F)
            .containerItem(emptyMilkCarton)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyColaPop = add("empty_cola_pop",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    colaPop = add("cola_pop",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 5), 1.0F)
            .containerItem(emptyColaPop)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyIronBrew = add("empty_iron_brew",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    ironBrew = add("iron_brew",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
            .containerItem(emptyIronBrew)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptySprite = add("empty_sprite",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sprite = add("sprite",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
            .containerItem(emptySprite)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    emptyZombieEnergy = add("empty_zombie_energy",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    zombieEnergy = add("zombie_energy",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .setUseAction(UseAction.DRINK)
            .effect(() -> new EffectInstance(ModEffects.HYDRATE.get(), 1, 8), 1.0F)
            .containerItem(emptyZombieEnergy)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    // ================================================================================
    // Medical
    // ================================================================================

    firstAidKit = add("first_aid_kit",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 1), 1.0F)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    adrenalineSyringe = add("adrenaline_syringe",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .effect(() -> new EffectInstance(Effects.SPEED, (20 * 20), 1), 1.0F)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    morphineSyringe = add("morphine_syringe",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    bandage = add("bandage",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    emptyBloodBag = add("empty_blood_bag",
        new FillableItem((FillableItem.Properties) new FillableItem.Properties()
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

    bloodBag = add("blood_bag",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
            .maxStackSize(1)
            .containerItem(emptyBloodBag)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    syringe = add("syringe",
        new FillableItem((FillableItem.Properties) new FillableItem.Properties()
            .setFullItem(new ResourceLocation(CraftingDead.ID, "rbi_syringe"))
            .setEntityPredicate((entity) -> entity instanceof ZombieEntity)
            .setProbability(0.25F)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    rbiSyringe = add("rbi_syringe",
        new Item(new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

    bottledRbi = add("bottled_rbi",
        new Item(new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

    bloodyRag = add("bloody_rag",
        new FillableItem((FillableItem.Properties) new FillableItem.Properties()
            .setFullItem(new ResourceLocation(CraftingDead.ID, "clean_rag"))
            .setBlockPredicate(
                (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    dirtyRag = add("dirty_rag",
        new FillableItem((FillableItem.Properties) new FillableItem.Properties()
            .setFullItem(new ResourceLocation(CraftingDead.ID, "clean_rag"))
            .setBlockPredicate(
                (blockPos, blockState) -> blockState.getFluidState().getFluid() == Fluids.WATER)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    cleanRag = add("clean_rag",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .containerItem(bloodyRag)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    splint = add("splint",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    cureSyringe = add("cure_syringe",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    bottledCure = add("bottled_cure",
        new Item(new Item.Properties().maxStackSize(1).group(ModItemGroups.CRAFTING_DEAD_MED)));

    antibiotics = add("antibiotics",
        new ConsumableItem((ConsumableItem.Properties) new ConsumableItem.Properties()
            .effect(() -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 0), 1.0F)
            .maxStackSize(1)
            .group(ModItemGroups.CRAFTING_DEAD_MED)));

    // ================================================================================
    // Weapon
    // ================================================================================

    crowbar = add("crowbar", new MeleeWeaponItem(3, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    bat = add("bat", new MeleeWeaponItem(5, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    katana = add("katana", new MeleeWeaponItem(18, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    pipe = add("pipe", new MeleeWeaponItem(9, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    rustypipe = add("rusty_pipe", new MeleeWeaponItem(9, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    fireaxe = add("fire_axe", new MeleeWeaponItem(14, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    chainsaw = add("chainsaw", new MeleeWeaponItem(8, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    bowie = add("bowie", new MeleeWeaponItem(15, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    golfclub = add("golf_club", new MeleeWeaponItem(6, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    nightstick = add("night_stick", new MeleeWeaponItem(4, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    sledgehammer = add("sledge_hammer", new MeleeWeaponItem(10, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    nailBat = add("nail_bat", new MeleeWeaponItem(8, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    shovel = add("shovel", new MeleeWeaponItem(8, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    hatchet = add("hatchet", new MeleeWeaponItem(16, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    broadsword = add("broad_sword", new MeleeWeaponItem(14, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    machete = add("machete", new MeleeWeaponItem(14, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    scytheweapon = add("scythe_weapon", new MeleeWeaponItem(15, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    scythe = add("scythe", new MeleeWeaponItem(20, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    pickaxe = add("pickaxe", new MeleeWeaponItem(10, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    bostaff = add("bo_staff", new MeleeWeaponItem(4, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    wrench = add("wrench", new MeleeWeaponItem(4, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    fryingPan = add("frying_pan", new MeleeWeaponItem(8, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    boltCutters = add("bolt_cutters", new MeleeWeaponItem(9, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    combatKnife = add("combat_knife", new MeleeWeaponItem(14, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    steelBat = add("steel_bat", new MeleeWeaponItem(7, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    cleaver = add("cleaver", new MeleeWeaponItem(10, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    brokenBottle = add("broken_bottle", new MeleeWeaponItem(15, -2.4F,
        new Item.Properties().maxStackSize(1).group((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    // ================================================================================
    // Wearable
    // ================================================================================

    smallRedBackpack = add("small_red_backpack", new BackpackItem(Backpack.SMALL));

    smallOrangeBackpack = add("small_orange_backpack", new BackpackItem(Backpack.SMALL));

    smallYellowBackpack = add("small_yellow_backpack", new BackpackItem(Backpack.SMALL));

    smallGreenBackpack = add("small_green_backpack", new BackpackItem(Backpack.SMALL));

    smallBlueBackpack = add("small_blue_backpack", new BackpackItem(Backpack.SMALL));

    smallPurpleBackpack = add("small_purple_backpack", new BackpackItem(Backpack.SMALL));

    mediumRedBackpack = add("medium_red_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumOrangeBackpack = add("medium_orange_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumYellowBackpack = add("medium_yellow_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumGreenBackpack = add("medium_green_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumBlueBackpack = add("medium_blue_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumPurpleBackpack = add("medium_purple_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumGreyBackpack = add("medium_grey_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumBlackBackpack = add("medium_black_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumGhillieBackpack = add("medium_ghillie_backpack", new BackpackItem(Backpack.MEDIUM));

    mediumWhiteBackpack = add("medium_white_backpack", new BackpackItem(Backpack.MEDIUM));

    largeGreyBackpack = add("large_grey_backpack", new BackpackItem(Backpack.LARGE));

    largeGreenBackpack = add("large_green_backpack", new BackpackItem(Backpack.LARGE));

    largeTanBackpack = add("large_tan_backpack", new BackpackItem(Backpack.LARGE));

    largeBlackBackpack = add("large_black_backpack", new BackpackItem(Backpack.LARGE));

    largeGhillieBackpack = add("large_ghillie_backpack", new BackpackItem(Backpack.LARGE));

    gunBagBackpack = add("gun_bag_backpack", new BackpackItem(Backpack.LARGE));

    greyGunBagBackpack = add("grey_gun_bag_backpack", new BackpackItem(Backpack.LARGE));

    ammoBagBackpack = add("ammo_backpack", new BackpackItem(Backpack.LARGE));
  }

  public static void register(RegistryEvent.Register<Item> event) {
    toRegister.forEach(event.getRegistry()::register);
  }

  private static Item add(String registryName, Item item) {
    toRegister.add(item.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName)));
    return item;
  }
}
