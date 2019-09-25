package com.craftingdead.mod.item;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.entity.ModEntityTypes;
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

  //TODO Perhaps they need to be renamed to be more convenient.
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
  public static Item ragDirty;
  public static Item ragClean;
  public static Item splint;
  public static Item needleCure;
  public static Item bottleCure;
  public static Item antibiotics;
  public static Item ragBloody;

  public static Item advancedZombieSpawnEgg;
  public static Item fastZombieSpawnEgg;
  public static Item tankZombieSpawnEgg;
  public static Item weakZombieSpawnEgg;

  public static Item grenadeSpawnEgg;

  public static void initialize() {

    // ================================================================================
    // Guns
    // ================================================================================

    acr = add("acr", new GunItem(new GunItem.Properties()
        .setFireRate(80)
        .setClipSize(0)
        .setDamage(7)
        .setReloadTime(2.2F)
        .setAccuracy(0.8F)
        .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
        .setShootSound(() -> ModSoundEvents.ACR_SHOOT)
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))));

    ak47 = add("ak47", new GunItem(new GunItem.Properties()
        .setFireRate(80)
        .setClipSize(0)
        .setDamage(7)
        .setReloadTime(2.2F)
        .setAccuracy(0.8F)
        .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
        .setShootSound(() -> ModSoundEvents.AK47_SHOOT)
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))));

    desertEagle = add("desert_eagle", new GunItem(new GunItem.Properties()
        .setFireRate(0)
        .setClipSize(0)
        .setDamage(8)
        .setReloadTime(2.2F)
        .setAccuracy(0.7F)
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
        .setShootSound(() -> ModSoundEvents.DESERT_EAGLE_SHOOT)
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    m4a1 = add("m4a1", new GunItem(new GunItem.Properties()
        .setFireRate(80)
        .setClipSize(0)
        .setDamage(7)
        .setReloadTime(2.2F)
        .setAccuracy(0.9F)
        .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI))
        .setShootSound(() -> ModSoundEvents.M4A1_SHOOT)
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))));

    m9 = add("m9", new GunItem(new GunItem.Properties()
        .setFireRate(80)
        .setClipSize(0)
        .setDamage(7)
        .setReloadTime(2.2F)
        .setAccuracy(0.9F)
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
        .setShootSound(() -> ModSoundEvents.M9_SHOOT)
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    taser = add("taser", new GunItem(new GunItem.Properties()
        .setFireRate(2000)
        .setClipSize(0)
        .setDamage(7)
        .setReloadTime(2.2F)
        .setAccuracy(0.9F)
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
        .setShootSound(() -> ModSoundEvents.TASER_SHOOT)
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    magnum = add("magnum", new GunItem(new GunItem.Properties()
        .setFireRate(80)
        .setClipSize(0)
        .setDamage(7)
        .setReloadTime(2.2F)
        .setAccuracy(0.9F)
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI))
        .setShootSound(() -> ModSoundEvents.MAGNUM_SHOOT)
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))));

    fn57 = add("fn57", new GunItem(new GunItem.Properties()
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

    canOpener = add("can_opener", new ToolItem(new Item.Properties()
        .maxDamage(8)
        .group(ModItemGroups.CRAFTING_DEAD_MISC)));

    screwdriver = add("screwdriver", new ToolItem(new Item.Properties()
        .maxDamage(4)
        .group(ModItemGroups.CRAFTING_DEAD_MISC)));

    multiTool = add("multi_tool", new MeleeWeaponItem(8, -2.4F, new Item.Properties()
        .maxDamage(20)
        .group(ModItemGroups.CRAFTING_DEAD_MISC)));

    // ================================================================================
    // Consumable
    // ================================================================================

    emptyWaterBottle = add("empty_water_bottle", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    waterBottle = add("water_bottle",
        new DrinkItem(8, () -> emptyWaterBottle, new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaOrangeEmpty = add("empty_soda_orange", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaOrange = add("soda_orange",
            new DrinkItem(4, () -> sodaOrangeEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    iceteaEmpty = add("empty_ice_tea", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    icetea = add("ice_tea",
            new DrinkItem(4, () -> iceteaEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    juicePouchEmpty = add("empty_juice_pouch", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    juicePouch = add("juice_pouch",
            new DrinkItem(2, () -> juicePouchEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaPepeEmpty = add("empty_soda_pepe", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaPepe = add("soda_pepe",
            new DrinkItem(4, () -> sodaPepeEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaDewEmpty = add("empty_soda_dew", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaDew = add("soda_dew",
            new DrinkItem(4, () -> sodaDewEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaColaEmpty = add("empty_soda_cola", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sodaCola = add("soda_cola",
            new DrinkItem(4, () -> sodaColaEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    milkCartonEmpty = add("empty_milk_carton", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    milkCarton = add("milk_carton",
            new DrinkItem(8, () -> milkCartonEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    milkRotten = add("milk_rotten",
            new DrinkItem(4, () -> milkCartonEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    colaPopEmpty = add("empty_cola_pop", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    colaPop = add("cola_pop",
            new DrinkItem(6, () -> colaPopEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    ironBrewEmpty = add("empty_iron_brew", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    ironBrew = add("iron_brew",
            new DrinkItem(9, () -> ironBrewEmpty, new Item.Properties()
                            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    spriteEmpty = add("empty_sprite", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    sprite = add("sprite",
            new DrinkItem(9, () -> spriteEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    zombieEnergyEmpty = add("empty_zombie_energy", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    zombieEnergy = add("zombie_energy",
            new DrinkItem(9, () -> zombieEnergyEmpty, new Item.Properties()
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    lemonFizzEmpty = add("empty_lemon_fizz", new Item(new Item.Properties()
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    lemonFizz = add("lemon_fizz",
            new DrinkItem(6, () -> lemonFizzEmpty, new Item.Properties()
                     .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    // ================================================================================
    // edible
    // ================================================================================

    mre = add("mre",
            new DrinkItem(8, new Item.Properties()
                    .food(ModFoods.MRE)
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    orange = add("orange",
            new DrinkItem(2, new Item.Properties()
                    .food(ModFoods.ORANGE)
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenOrange = add("rotten_orange",
            new DrinkItem(1, new Item.Properties()
                    .food(ModFoods.ROTTENORANGE)
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    pear = add("pear",
            new DrinkItem(3, new Item.Properties()
                    .food(ModFoods.PEAR)
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    rottenPear  = add("rotten_pear",
            new DrinkItem(1, new Item.Properties()
                    .food(ModFoods.ROTTENOPEAR)
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    apple = add("apple",
            new DrinkItem(3, new Item.Properties()
                    .food(ModFoods.APPLE)
                    .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    riceBag = add("rice_bag", new DrinkItem(new Item.Properties()
            .food(ModFoods.RICEBAG)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    powerBar = add("power_bar", new DrinkItem(new Item.Properties()
        .food(ModFoods.POWER_BAR)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    candyBar = add("candy_bar", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANDY_BAR)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cereal = add("cereal", new DrinkItem(new Item.Properties()
        .food(ModFoods.CEREAL)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedCorn = add("canned_corn", new DrinkItem(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedCorn = add("open_canned_corn", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANNED_CORN)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedBeans = add("canned_beans", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedBeans = add("open_canned_beans", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANNED_BEANS)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedTuna = add("canned_tuna", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedTuna = add("open_canned_tuna", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANNED_TUNA)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedPeach = add("canned_peach", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedPeach = add("open_canned_peach",
        new DrinkItem(2, null, new Item.Properties()
            .food(ModFoods.CANNED_PEACH)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedPasta = add("canned_pasta", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedPasta = add("open_canned_pasta", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANNED_PASTA)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedBacon = add("canned_bacon", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedBacon = add("open_canned_bacon", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANNED_BACON)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedCustard = add("canned_custard", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedCustard = add("open_canned_custard",
        new DrinkItem(4, null, new Item.Properties()
            .food(ModFoods.CANNED_CUSTARD)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedPickles = add("canned_pickles", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedPickles = add("open_canned_pickles", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANNED_PICKLES)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedDogFood = add("canned_dog_food", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedDogFood = add("open_canned_dog_food", new DrinkItem(new Item.Properties()
        .food(ModFoods.CANNED_DOG_FOOD)
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    cannedTomatoSoup = add("canned_tomato_soup", new Item(new Item.Properties()
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    openCannedTomatoSoup = add("open_canned_tomato_soup",
        new DrinkItem(3, null, new Item.Properties()
            .food(ModFoods.CANNED_TOMATO_SOUP)
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));
    // ================================================================================
    // medic
    // ================================================================================

    medpack = add("med_pack",
            new MedItem( new MedItem.Properties()
            .setBloodHeal(8)
            .setBrokenLag()
            .setMaxStackSize(1)
            .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    needleAdrenaline = add("andrenaline_syringe",
            new MedItem( new MedItem.Properties()
                    .setAdrenaline()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    needleMorphine = add("morphine_syringe",
            new MedItem( new MedItem.Properties()
                    .setBrokenLag()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));


    bandage = add("bandage",
            new MedItem( new MedItem.Properties()
                    .setBloodHeal(2)
                    .setBleeding()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    bloodbag = add("blood_bag",
            new MedItem( new MedItem.Properties()
                    .setBloodHeal(4)
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    bloodbagEmpty = add("empty_blood_bag",
            new MedItem( new MedItem.Properties()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    needle = add("needle",
            new MedItem( new MedItem.Properties()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    needleRBI = add("needle_rbi",
            new MedItem( new MedItem.Properties()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    bottleRBI = add("bottle_rbi",
            new MedItem( new MedItem.Properties()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    ragClean = add("rag_clean",
            new MedItem( new MedItem.Properties()
                    .setBleeding()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    //TODO You need to add her ability to clear when she clicks on the water.
    ragDirty = add("rag_dirty",
            new MedItem( new MedItem.Properties()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    splint = add("splint",
            new MedItem( new MedItem.Properties()
                    .setBrokenLag()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    needleCure = add("needle_cure",
            new MedItem( new MedItem.Properties()
                    .setInfection()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    bottleCure = add("bottle_cure",
            new MedItem( new MedItem.Properties()
                    .setInfection()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    antibiotics  = add("antibiotics",
            new MedItem( new MedItem.Properties()
                    .setBloodHeal(4)
                    .setBrokenLag()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    //TODO You need to add her ability to clear when she clicks on the water.
    ragBloody = add("rag_bloody",
            new MedItem( new MedItem.Properties()
                    .setMaxStackSize(1)
                    .setGroup(ModItemGroups.CRAFTING_DEAD_CONSUMABLES)));

    // ================================================================================
    // Spawn Eggs
    // ================================================================================

    advancedZombieSpawnEgg = add("advanced_zombie_spawn_egg",
        new SpawnEggItem(ModEntityTypes.advancedZombie, 0x000000, 0xFFFFFF,
            new Item.Properties()
                .group(ModItemGroups.CRAFTING_DEAD_MISC)));

    fastZombieSpawnEgg = add("fast_zombie_spawn_egg",
        new SpawnEggItem(ModEntityTypes.fastZombie, 0x000000, 0xFFFFFF,
            new Item.Properties()
                .group(ModItemGroups.CRAFTING_DEAD_MISC)));

    tankZombieSpawnEgg = add("tank_zombie_spawn_egg",
        new SpawnEggItem(ModEntityTypes.tankZombie, 0x000000, 0xFFFFFF,
            new Item.Properties()
                .group(ModItemGroups.CRAFTING_DEAD_MISC)));

    weakZombieSpawnEgg = add("weak_zombie_spawn_egg",
        new SpawnEggItem(ModEntityTypes.weakZombie, 0x000000, 0xFFFFFF,
            new Item.Properties()
                .group(ModItemGroups.CRAFTING_DEAD_MISC)));
  }

  public static void register(RegistryEvent.Register<Item> event) {
    toRegister.forEach(event.getRegistry()::register);
  }

  private static Item add(String registryName, Item item) {
    toRegister.add(item.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName)));
    return item;
  }
}
