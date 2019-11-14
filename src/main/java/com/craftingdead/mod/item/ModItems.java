package com.craftingdead.mod.item;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.entity.ModEntityTypes;
import com.craftingdead.mod.type.Backpack;
import com.craftingdead.mod.util.ModSoundEvents;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class ModItems {

  private static final List<Item> toRegister = new ArrayList<>();

  public static Item acr;
  public static Item ak47;
  public static Item desertEagle;
  public static Item m4a1;
  public static Item m9;
  public static Item taser;
  public static Item magnum;
  public static Item fn57;

  public static Item canOpener;
  public static Item screwdriver;
  public static Item multiTool;

  public static Item emptyWaterBottle;
  public static Item waterBottle;
  public static Item powerBar;
  public static Item candyBar;
  public static Item cereal;
  public static Item cannedCorn;
  public static Item openCannedCorn;
  public static Item cannedBeans;
  public static Item openCannedBeans;
  public static Item cannedTuna;
  public static Item openCannedTuna;
  public static Item cannedPeach;
  public static Item openCannedPeach;
  public static Item cannedPasta;
  public static Item openCannedPasta;
  public static Item cannedBacon;
  public static Item openCannedBacon;
  public static Item cannedCustard;
  public static Item openCannedCustard;
  public static Item cannedPickles;
  public static Item openCannedPickles;
  public static Item cannedDogFood;
  public static Item openCannedDogFood;
  public static Item cannedTomatoSoup;
  public static Item openCannedTomatoSoup;

  // TODO Perhaps they need to be renamed to be more convenient.
  public static Item sodaOrange;
  public static Item sodaOrangeEmpty;
  public static Item icetea;
  public static Item iceteaEmpty;
  public static Item juicePouch;
  public static Item juicePouchEmpty;
  public static Item sodaPepe;
  public static Item sodaPepeEmpty;
  public static Item sodaDewEmpty;
  public static Item sodaDew;
  public static Item sodaColaEmpty;
  public static Item sodaCola;
  public static Item milkCartonEmpty;
  public static Item milkCarton;
  public static Item milkRotten;
  public static Item colaPopEmpty;
  public static Item colaPop;
  public static Item ironBrewEmpty;
  public static Item ironBrew;
  public static Item spriteEmpty;
  public static Item sprite;
  public static Item zombieEnergyEmpty;
  public static Item zombieEnergy;
  public static Item lemonFizzEmpty;
  public static Item lemonFizz;
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
  public static Item chipsRanch;
  public static Item chipsCheese;
  public static Item chipsSalt;
  public static Item popcorn;
  public static Item cerealNutty;
  public static Item cerealEmerald;
  public static Item cerealFlakes;
  public static Item watercanteenEmpty;
  public static Item watercanteen;
  public static Item flask;
  public static Item flaskEmpty;
  public static Item flasktea;
  public static Item flaskCoffee;

  // medic
  public static Item bloodbag;
  public static Item bloodbagEmpty;
  public static Item medpack;
  public static Item needleAdrenaline;
  public static Item needle;
  public static Item needleMorphine;
  public static Item bandage;
  public static Item needleRBI;
  public static Item bottleRBI;
  public static Item dirtyRag;
  public static Item cleanRag;
  public static Item splint;
  public static Item needleCure;
  public static Item bottleCure;
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
  public static Item sludgehammer;
  public static Item batNail;
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
  public static Item backpackSmallRed;
  public static Item backpackSmallOrange;
  public static Item backpackSmallYellow;
  public static Item backpackSmallGreen;
  public static Item backpackSmallBlue;
  public static Item backpackSmallPurple;
  public static Item backpackMediumRed;
  public static Item backpackMediumOrange;
  public static Item backpackMediumYellow;
  public static Item backpackMediumGreen;
  public static Item backpackMediumBlue;
  public static Item backpackMediumPurple;
  public static Item backpackMediumGrey;
  public static Item backpackMediumBlack;
  public static Item backpackMediumGhillie;
  public static Item backpackMediumWhite;
  public static Item backpackLargeGrey;
  public static Item backpackLargeGreen;
  public static Item backpackLargeTan;
  public static Item backpackLargeBlack;
  public static Item backpackLargeGhillie;
  public static Item backpackGunBag;
  public static Item backpackGunBagGrey;
  public static Item backpackAmmoBag;

  public static Item advancedZombieSpawnEgg;
  public static Item fastZombieSpawnEgg;
  public static Item tankZombieSpawnEgg;
  public static Item weakZombieSpawnEgg;

  //Drop
  public static Item dropmed;
  public static Item dropmil;
  public static Item dropsupp;

  public static void initialize() {

    // ================================================================================
    // Air Drop
    // ================================================================================

    dropmed = add("drop_med", new AirDropItem(new AirDropItem.Properties()
        .setMaxStackSize(1)
        .setMedical()
        .setGroup(ModItemGroups.CRAFTING_DEAD_GENERAL)));

    dropmil = add("drop_mil", new AirDropItem(new AirDropItem.Properties()
        .setMaxStackSize(1)
        .setMilitary()
        .setGroup(ModItemGroups.CRAFTING_DEAD_GENERAL)));

    dropsupp = add("drop_supp", new AirDropItem(new AirDropItem.Properties()
        .setMaxStackSize(1)
        .setSupply()
        .setGroup(ModItemGroups.CRAFTING_DEAD_GENERAL)));


    // ================================================================================
    // Guns
    // ================================================================================

    acr = add("acr",
        new GunItem(new GunItem.Properties()
            .setFireRate(80)
            .setClipSize(0)
            .setDamage(7)
            .setReloadTime(2.2F)
            .setAccuracy(0.8F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.ACR_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))));

    ak47 = add("ak47",
        new GunItem(new GunItem.Properties()
            .setFireRate(80)
            .setClipSize(0)
            .setDamage(7)
            .setReloadTime(2.2F)
            .setAccuracy(0.8F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.AK47_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))));

    desertEagle = add("desert_eagle",
        new GunItem(new GunItem.Properties()
            .setFireRate(0)
            .setClipSize(0)
            .setDamage(8)
            .setReloadTime(2.2F)
            .setAccuracy(0.7F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.DESERT_EAGLE_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    m4a1 = add("m4a1",
        new GunItem(new GunItem.Properties()
            .setFireRate(80)
            .setClipSize(0)
            .setDamage(7)
            .setReloadTime(2.2F)
            .setAccuracy(0.9F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.M4A1_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))));

    m9 = add("m9",
        new GunItem(new GunItem.Properties()
            .setFireRate(80)
            .setClipSize(0)
            .setDamage(7)
            .setReloadTime(2.2F)
            .setAccuracy(0.9F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.M9_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    taser = add("taser",
        new GunItem(new GunItem.Properties()
            .setFireRate(2000)
            .setClipSize(0)
            .setDamage(7)
            .setReloadTime(2.2F)
            .setAccuracy(0.9F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.TASER_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    magnum = add("magnum",
        new GunItem(new GunItem.Properties()
            .setFireRate(80)
            .setClipSize(0)
            .setDamage(7)
            .setReloadTime(2.2F)
            .setAccuracy(0.9F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.MAGNUM_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    fn57 = add("fn57",
        new GunItem(new GunItem.Properties()
            .setFireRate(80)
            .setClipSize(0)
            .setDamage(7)
            .setReloadTime(2.2F)
            .setAccuracy(0.9F)
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
            .setShootSound(() -> ModSoundEvents.FN57_SHOOT)
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    // ================================================================================
    // Tools
    // ================================================================================

    canOpener = add("can_opener",
        new ToolItem(new Item.Properties().maxDamage(8).group(ModItemGroups.CRAFTING_DEAD_MISC)));

    screwdriver = add("screwdriver",
        new ToolItem(new Item.Properties().maxDamage(4).group(ModItemGroups.CRAFTING_DEAD_MISC)));

    multiTool = add("multi_tool", new MeleeWeaponItem(8, -2.4F,
        new Item.Properties().maxDamage(20).group(ModItemGroups.CRAFTING_DEAD_MISC)));

    // ================================================================================
    // Consumable
    // ================================================================================

    emptyWaterBottle = add("empty_water_bottle",
        new Item(new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    waterBottle = add("water_bottle",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> emptyWaterBottle)
            .setWater(8)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaOrangeEmpty = add("empty_soda_orange",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaOrange = add("soda_orange",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> sodaOrangeEmpty)
            .setWater(4)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    iceteaEmpty = add("empty_ice_tea",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    icetea = add("ice_tea",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> iceteaEmpty)
            .setWater(4)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    juicePouchEmpty = add("empty_juice_pouch",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    juicePouch = add("juice_pouch",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> juicePouchEmpty)
            .setWater(2)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaPepeEmpty = add("empty_soda_pepe",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaPepe = add("soda_pepe",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> sodaPepeEmpty)
            .setWater(4)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaDewEmpty = add("empty_soda_dew",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaDew = add("soda_dew",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> sodaDewEmpty)
            .setWater(4)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaColaEmpty = add("empty_soda_cola",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaCola = add("soda_cola",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> sodaColaEmpty)
            .setWater(4)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    milkCartonEmpty = add("empty_milk_carton",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    milkCarton = add("milk_carton",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> milkCartonEmpty)
            .setWater(8)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    milkRotten = add("milk_rotten",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> milkCartonEmpty)
            .setWater(4)
            .setRotten()
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    colaPopEmpty = add("empty_cola_pop",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    colaPop = add("cola_pop",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> colaPopEmpty)
            .setWater(6)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    ironBrewEmpty = add("empty_iron_brew",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    ironBrew = add("iron_brew",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> ironBrewEmpty)
            .setWater(9)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    spriteEmpty = add("empty_sprite",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sprite = add("sprite",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> spriteEmpty)
            .setWater(9)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    zombieEnergyEmpty = add("empty_zombie_energy",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    zombieEnergy = add("zombie_energy",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> zombieEnergyEmpty)
            .setWater(9)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    lemonFizzEmpty = add("empty_lemon_fizz",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    lemonFizz = add("lemon_fizz",
        new EatItem(new EatItem.Properties()
            .setContainer(() -> lemonFizzEmpty)
            .setWater(6)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    // ================================================================================
    // eat
    // ================================================================================

    mre = add("mre",
        new EatItem(new EatItem.Properties()
            .setWater(8)
            .setFood(ModFoods.MRE)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    orange = add("orange",
        new EatItem(new EatItem.Properties()
            .setWater(2)
            .setFood(ModFoods.ORANGE)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenOrange = add("rotten_orange",
        new EatItem(new EatItem.Properties()
            .setWater(1)
            .setFood(ModFoods.ROTTENORANGE)
            .setRotten()
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    pear = add("pear",
        new EatItem(new EatItem.Properties()
            .setWater(3)
            .setFood(ModFoods.PEAR)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenPear = add("rotten_pear",
        new EatItem(new EatItem.Properties()
            .setWater(1)
            .setFood(ModFoods.ROTTENOPEAR)
            .setRotten()
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    apple = add("apple",
        new EatItem(new EatItem.Properties()
            .setWater(3)
            .setFood(ModFoods.APPLE)
            .setRotten()
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenApple = add("rotten_apple",
        new EatItem(new EatItem.Properties()
            .setWater(1)
            .setFood(ModFoods.ROTTENAPPLE)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    riceBag = add("rice_bag",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.RICEBAG)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    powerBar = add("power_bar",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.POWER_BAR)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    candyBar = add("candy_bar",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANDY_BAR)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cereal = add("cereal",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CEREAL)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedCorn = add("canned_corn",
        new EatItem(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedCorn = add("open_canned_corn",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANNED_CORN)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedBeans = add("canned_beans",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedBeans = add("open_canned_beans",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANNED_BEANS)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedTuna = add("canned_tuna",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedTuna = add("open_canned_tuna",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANNED_TUNA)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedPeach = add("canned_peach",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedPeach = add("open_canned_peach",
        new EatItem(new EatItem.Properties()
            .setWater(2)
            .setFood(ModFoods.CANNED_PEACH)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedPasta = add("canned_pasta",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedPasta = add("open_canned_pasta",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANNED_PASTA)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedBacon = add("canned_bacon",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedBacon = add("open_canned_bacon",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANNED_BACON)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedCustard = add("canned_custard",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedCustard = add("open_canned_custard",
        new EatItem(new EatItem.Properties()
            .setWater(4)
            .setFood(ModFoods.CANNED_CUSTARD)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedPickles = add("canned_pickles",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedPickles = add("open_canned_pickles",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANNED_PICKLES)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedDogFood = add("canned_dog_food",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedDogFood = add("open_canned_dog_food",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CANNED_DOG_FOOD)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedTomatoSoup = add("canned_tomato_soup",
        new Item(new EatItem.Properties().setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedTomatoSoup = add("open_canned_tomato_soup",
        new EatItem(new EatItem.Properties()
            .setWater(3)
            .setFood(ModFoods.CANNED_TOMATO_SOUP)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    noodles = add("noodles",
        new EatItem(new EatItem.Properties()
            .setWater(4)
            .setFood(ModFoods.NOODLECUP)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    watermelon = add("water_melon",
        new EatItem(new EatItem.Properties()
            .setWater(6)
            .setFood(ModFoods.WATERMELON)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenWatermelon = add("rotten_water_melon",
        new EatItem(new EatItem.Properties()
            .setWater(2)
            .setFood(ModFoods.ROTTENWATERMELON)
            .setRotten()
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    blueberry = add("blue_berry",
        new EatItem(new EatItem.Properties()
            .setWater(4)
            .setFood(ModFoods.BLUEBERRIES)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenBlueberry = add("rotten_blue_berry",
        new EatItem(new EatItem.Properties()
            .setWater(2)
            .setFood(ModFoods.ROTTENBLUEBERRIES)
            .setRotten()
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    raspberry = add("raspberry",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.RASPBERRIES)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenRaspberry = add("rotten_raspberry",
        new EatItem(new EatItem.Properties()
            .setWater(2)
            .setFood(ModFoods.ROTTENRASPBERRIES)
            .setRotten()
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    chips = add("chips",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CHIPS)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    chipsRanch = add("ranch_chips",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.RANCHCHIPS)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    chipsCheese = add("cheese_chips",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CHEESECHIPS)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    chipsSalt = add("salt_chips",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.SALTCHIPS)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    popcorn = add("popcorn",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.POPCORN)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cerealNutty = add("cereal_nutty",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CEREALNUTTY)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cerealEmerald = add("cereal_emerald",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CEREALEMERALD)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cerealFlakes = add("cereal_flakes",
        new EatItem(new EatItem.Properties()
            .setFood(ModFoods.CEREALFLAKES)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    watercanteenEmpty = add("empty_water_canteen",
        new CanteenItem(new EatItem.Properties()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    watercanteen = add("water_canteen",
        new CanteenItem(new EatItem.Properties()
            .setWater(9)
            .setContainer(watercanteenEmpty)
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    flaskEmpty = add("empty_flask",
        new FlaskItem(new EatItem.Properties()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    flask = add("flask",
        new FlaskItem(new EatItem.Properties()
            .setWater(6)
            .setContainer(flaskEmpty)
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    flasktea = add("flask_tea",
        new FlaskItem(new EatItem.Properties()
            .setWater(7)
            .setContainer(flaskEmpty)
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    flaskCoffee = add("flask_coffee",
        new FlaskItem(new EatItem.Properties()
            .setWater(8)
            .setContainer(flaskEmpty)
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));
    // ================================================================================
    // medic
    // ================================================================================
    medpack = add("med_pack",
        new MedicalItem(new MedicalItem.Properties()
            .setHealth(8)
            .setHealBrokenLeg()
            .setStopBleeding()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    needleAdrenaline = add("andrenaline_syringe",
        new MedicalItem(new MedicalItem.Properties()
            .setAdrenaline()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    needleMorphine = add("morphine_syringe",
        new MedicalItem(new MedicalItem.Properties()
            .setHealBrokenLeg()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    bandage = add("bandage",
        new MedicalItem(new MedicalItem.Properties()
            .setHealth(2)
            .setStopBleeding()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    bloodbagEmpty = add("empty_blood_bag", new MedicalItem(
        new MedicalItem.Properties().setMaxStackSize(1).setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    bloodbag = add("blood_bag",
        new MedicalItem(new MedicalItem.Properties()
            .setHealth(4)
            .setMaxStackSize(1)
            .setContainer(bloodbagEmpty)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    needle = add("needle", new MedicalItem(
        new MedicalItem.Properties().setMaxStackSize(1).setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    needleRBI = add("needle_rbi", new MedicalItem(
        new MedicalItem.Properties().setMaxStackSize(1).setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    bottleRBI = add("bottle_rbi", new MedicalItem(
        new MedicalItem.Properties().setMaxStackSize(1).setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    bloodyRag = add("rag_bloody", new RagItem(
        new MedicalItem.Properties().setMaxStackSize(1).setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    dirtyRag = add("rag_dirty", new RagItem(
        new MedicalItem.Properties().setMaxStackSize(1).setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    cleanRag = add("rag_clean",
        new RagItem(new MedicalItem.Properties()
            .setStopBleeding()
            .setMaxStackSize(1)
            .setContainer(bloodyRag)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    splint = add("splint",
        new MedicalItem(new MedicalItem.Properties()
            .setHealBrokenLeg()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    needleCure = add("needle_cure",
        new MedicalItem(new MedicalItem.Properties()
            .setInfection()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    bottleCure = add("bottle_cure",
        new MedicalItem(new MedicalItem.Properties()
            .setInfection()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    antibiotics = add("antibiotics",
        new MedicalItem(new MedicalItem.Properties()
            .setHealth(4)
            .setHealBrokenLeg()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_MED)));

    // ================================================================================
    // Weapon
    // ================================================================================

    crowbar = add("crowbar",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(3)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    bat = add("bat",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(5)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    katana = add("katana",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(18)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    pipe = add("pipe",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(9)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    rustypipe = add("rusty_pipe",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(9)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    fireaxe = add("fire_axe",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(14)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    chainsaw = add("chainsaw",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(8)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    bowie = add("bowie",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(15)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    golfclub = add("golf_club",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(6)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    nightstick = add("night_stick",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(4)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    sludgehammer = add("sludge_hammer",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(10)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    batNail = add("bat_nail",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(8)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    shovel = add("shovel",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(8)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    hatchet = add("hatchet",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(16)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    broadsword = add("broad_sword",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(14)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    machete = add("machete",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(14)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    scytheweapon = add("scythe_weapon",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(15)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    scythe = add("scythe",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(20)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    pickaxe = add("pickaxe",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(10)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    bostaff = add("bo_staff",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(4)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    wrench = add("wrench",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(4)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    fryingPan = add("frying_pan",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(8)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    boltCutters = add("bolt_cutters",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(9)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    combatKnife = add("combat_knife",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(14)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    steelBat = add("steel_bat",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(7)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    cleaver = add("cleaver",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(10)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    brokenBottle = add("broken_bottle",
        new WeaponItem(new WeaponItem.Properties()
            .setAttackDamageln(15)
            .setAttackSpeedln(-2.4F)
            .setMaxStackSize(1)
            .setTierItem(WeaponTier.DIAMOND)
            .setGroup((ModItemGroups.CRAFTING_DEAD_WEAPON))));

    // ================================================================================
    // wearable
    // ================================================================================

    backpackSmallRed = add("backpack_small_red", new BackpackItem(Backpack.SMALL));

    backpackSmallOrange = add("backpack_small_orange", new BackpackItem(Backpack.SMALL));

    backpackSmallYellow = add("backpack_small_yellow", new BackpackItem(Backpack.SMALL));

    backpackSmallGreen = add("backpack_small_green", new BackpackItem(Backpack.SMALL));

    backpackSmallBlue = add("backpack_small_blue", new BackpackItem(Backpack.SMALL));

    backpackSmallPurple = add("backpack_small_purple", new BackpackItem(Backpack.SMALL));

    backpackMediumRed = add("backpack_medium_red", new BackpackItem(Backpack.MEDIUM));

    backpackMediumOrange = add("backpack_medium_orange", new BackpackItem(Backpack.MEDIUM));

    backpackMediumYellow = add("backpack_medium_yellow", new BackpackItem(Backpack.MEDIUM));

    backpackMediumGreen = add("backpack_medium_green", new BackpackItem(Backpack.MEDIUM));

    backpackMediumBlue = add("backpack_medium_blue", new BackpackItem(Backpack.MEDIUM));

    backpackMediumPurple = add("backpack_medium_purple", new BackpackItem(Backpack.MEDIUM));

    backpackMediumGrey = add("backpack_medium_grey", new BackpackItem(Backpack.MEDIUM));

    backpackMediumBlack = add("backpack_medium_black", new BackpackItem(Backpack.MEDIUM));

    backpackMediumGhillie = add("backpack_medium_ghillie", new BackpackItem(Backpack.MEDIUM));

    backpackMediumWhite = add("backpack_medium_white", new BackpackItem(Backpack.MEDIUM));

    backpackLargeGrey = add("backpack_large_grey", new BackpackItem(Backpack.LARGE));

    backpackLargeGreen = add("backpack_large_green", new BackpackItem(Backpack.LARGE));

    backpackLargeTan = add("backpack_large_tan", new BackpackItem(Backpack.LARGE));

    backpackLargeBlack = add("backpack_large_black", new BackpackItem(Backpack.LARGE));

    backpackLargeGhillie = add("backpack_large_ghillie", new BackpackItem(Backpack.LARGE));

    backpackGunBag = add("backpack_gunbag", new BackpackItem(Backpack.LARGE));

    backpackGunBagGrey = add("backpack_gunbag_grey", new BackpackItem(Backpack.LARGE));

    backpackAmmoBag = add("backpack_ammo", new BackpackItem(Backpack.LARGE));

    // ================================================================================
    // Spawn Eggs
    // ================================================================================

    advancedZombieSpawnEgg =
        add("advanced_zombie_spawn_egg", new SpawnEggItem(ModEntityTypes.advancedZombie, 0x000000,
            0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

    fastZombieSpawnEgg = add("fast_zombie_spawn_egg", new SpawnEggItem(ModEntityTypes.fastZombie,
        0x000000, 0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

    tankZombieSpawnEgg = add("tank_zombie_spawn_egg", new SpawnEggItem(ModEntityTypes.tankZombie,
        0x000000, 0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));

    weakZombieSpawnEgg = add("weak_zombie_spawn_egg", new SpawnEggItem(ModEntityTypes.weakZombie,
        0x000000, 0xFFFFFF, new Item.Properties().group(ModItemGroups.CRAFTING_DEAD_MISC)));
  }

  public static void register(RegistryEvent.Register<Item> event) {
    toRegister.forEach(event.getRegistry()::register);
  }

  private static Item add(String registryName, Item item) {
    toRegister.add(item.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName)));
    return item;
  }
}
