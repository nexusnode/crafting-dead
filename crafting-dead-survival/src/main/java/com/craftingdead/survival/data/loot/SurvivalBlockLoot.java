/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

 package com.craftingdead.survival.data.loot;

 import java.util.stream.Collectors;
 import net.minecraft.world.item.Item;
 import com.craftingdead.core.world.item.ModItems;
 import com.craftingdead.survival.world.item.SurvivalItems;
 import com.craftingdead.survival.world.level.block.SurvivalBlocks;
 import net.minecraft.data.loot.BlockLoot;
 import net.minecraft.world.item.Items;
 import net.minecraft.world.level.block.Block;
 import net.minecraft.world.level.storage.loot.LootPool;
 import net.minecraft.world.level.storage.loot.LootTable;
 import net.minecraft.world.level.storage.loot.entries.LootItem;
 import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
 import net.minecraftforge.registries.RegistryObject;


 public class SurvivalBlockLoot extends BlockLoot {

    private boolean isImmerseLoaded;
    private Object immerseEmptyWaterBottle;
    private Object immerseEmptyWaterCanteen;
    private Object immerseWaterCanteen;
    private Object immersePowerBar;
    private Object immerseCandyBar;
    private Object immerseCereal;
    private Object immerseCannedSweetcorn;
    private Object immerseOpenCannedSweetcorn;
    private Object immerseCannedBeans;
    private Object immerseOpenCannedBeans;
    private Object immerseCannedTuna;
    private Object immerseOpenCannedTuna;
    private Object immerseCannedPeaches;
    private Object immerseOpenCannedPeaches;
    private Object immerseCannedPasta;
    private Object immerseOpenCannedPasta;
    private Object immerseCannedCornedBeef;
    private Object immerseOpenCannedCornedBeef;
    private Object immerseCannedCustard;
    private Object immerseOpenCannedCustard;
    private Object immerseCannedPickles;
    private Object immerseOpenCannedPickles;
    private Object immerseCannedDogFood;
    private Object immerseOpenCannedDogFood;
    private Object immerseCannedTomatoSoup;
    private Object immerseOpenCannedTomatoSoup;
    private Object immerseMRE;
    private Object immerseOrange;
    private Object immerseRottenOrange;
    private Object immersePear;
    private Object immerseRottenPear;
    private Object immerseRiceBag;
    private Object immerseNoodles;
    private Object immerseRottenMelonSlice;
    private Object immerseBlueberry;
    private Object immerseRottenBlueberry;
    private Object immerseRaspberry;
    private Object immerseRottenRaspberry;
    private Object immerseChips;
    private Object immerseRanchChips;
    private Object immerseCheesyChips;
    private Object immerseSaltedChips;
    private Object immersePopcorn;
    private Object immerseNuttyCereal;
    private Object immerseEmeraldCereal;
    private Object immerseFlakeCereal;
    private Object immerseCanOpener;
    private Object immerseScrewdriver;
    private Object immerseMultiTool;
  
    public SurvivalBlockLoot() {
      try {
        Class<?> immerseItemsClass = Class.forName("com.craftingdead.immerse.world.item.ImmerseItems");
        immerseEmptyWaterCanteen = immerseItemsClass.getField("EMPTY_WATER_CANTEEN").get(null);
        immerseWaterCanteen = immerseItemsClass.getField("WATER_CANTEEN").get(null);
        immerseEmptyWaterBottle = immerseItemsClass.getField("EMPTY_WATER_BOTTLE").get(null);
        immerseEmptyWaterCanteen = immerseItemsClass.getField("EMPTY_WATER_CANTEEN").get(null);
        immersePowerBar = immerseItemsClass.getField("POWER_BAR").get(null);
        immerseCandyBar = immerseItemsClass.getField("CANDY_BAR").get(null);
        immerseCereal = immerseItemsClass.getField("CEREAL").get(null);
        immerseCannedSweetcorn = immerseItemsClass.getField("CANNED_SWEETCORN").get(null);
        immerseOpenCannedSweetcorn = immerseItemsClass.getField("OPEN_CANNED_SWEETCORN").get(null);
        immerseCannedBeans = immerseItemsClass.getField("CANNED_BEANS").get(null);
        immerseOpenCannedBeans = immerseItemsClass.getField("OPEN_CANNED_BEANS").get(null);
        immerseCannedTuna = immerseItemsClass.getField("CANNED_TUNA").get(null);
        immerseOpenCannedTuna = immerseItemsClass.getField("OPEN_CANNED_TUNA").get(null);
        immerseCannedPeaches = immerseItemsClass.getField("CANNED_PEACHES").get(null);
        immerseOpenCannedPeaches = immerseItemsClass.getField("OPEN_CANNED_PEACHES").get(null);
        immerseCannedPasta = immerseItemsClass.getField("CANNED_PASTA").get(null);
        immerseOpenCannedPasta = immerseItemsClass.getField("OPEN_CANNED_PASTA").get(null);
        immerseCannedCornedBeef = immerseItemsClass.getField("CANNED_CORNED_BEEF").get(null);
        immerseOpenCannedCornedBeef = immerseItemsClass.getField("OPEN_CANNED_CORNED_BEEF").get(null);
        immerseCannedCustard = immerseItemsClass.getField("CANNED_CUSTARD").get(null);
        immerseOpenCannedCustard = immerseItemsClass.getField("OPEN_CANNED_CUSTARD").get(null);
        immerseCannedPickles = immerseItemsClass.getField("CANNED_PICKLES").get(null);
        immerseOpenCannedPickles = immerseItemsClass.getField("OPEN_CANNED_PICKLES").get(null);
        immerseCannedDogFood = immerseItemsClass.getField("CANNED_DOG_FOOD").get(null);
        immerseOpenCannedDogFood = immerseItemsClass.getField("OPEN_CANNED_DOG_FOOD").get(null);
        immerseCannedTomatoSoup = immerseItemsClass.getField("CANNED_TOMATO_SOUP").get(null);
        immerseOpenCannedTomatoSoup = immerseItemsClass.getField("OPEN_CANNED_TOMATO_SOUP").get(null);
        immerseMRE = immerseItemsClass.getField("MRE").get(null);
        immerseOrange = immerseItemsClass.getField("ORANGE").get(null);
        immerseRottenOrange = immerseItemsClass.getField("ROTTEN_ORANGE").get(null);
        immersePear = immerseItemsClass.getField("PEAR").get(null);
        immerseRottenPear = immerseItemsClass.getField("ROTTEN_PEAR").get(null);
        immerseRiceBag = immerseItemsClass.getField("RICE_BAG").get(null);
        immerseNoodles = immerseItemsClass.getField("NOODLES").get(null);
        immerseRottenMelonSlice = immerseItemsClass.getField("ROTTEN_MELON_SLICE").get(null);
        immerseBlueberry = immerseItemsClass.getField("BLUEBERRY").get(null);
        immerseRottenBlueberry = immerseItemsClass.getField("ROTTEN_BLUEBERRY").get(null);
        immerseRaspberry = immerseItemsClass.getField("RASPBERRY").get(null);
        immerseRottenRaspberry = immerseItemsClass.getField("ROTTEN_RASPBERRY").get(null);
        immerseChips = immerseItemsClass.getField("CHIPS").get(null);
        immerseRanchChips = immerseItemsClass.getField("RANCH_CHIPS").get(null);
        immerseCheesyChips = immerseItemsClass.getField("CHEESY_CHIPS").get(null);
        immerseSaltedChips = immerseItemsClass.getField("SALTED_CHIPS").get(null);
        immersePopcorn = immerseItemsClass.getField("POPCORN").get(null);
        immerseNuttyCereal = immerseItemsClass.getField("NUTTY_CEREAL").get(null);
        immerseEmeraldCereal = immerseItemsClass.getField("EMERALD_CEREAL").get(null);
        immerseFlakeCereal = immerseItemsClass.getField("FLAKE_CEREAL").get(null);
        immerseCanOpener = immerseItemsClass.getField("CAN_OPENER").get(null);
        immerseScrewdriver = immerseItemsClass.getField("SCREWDRIVER").get(null);
        immerseMultiTool = immerseItemsClass.getField("MULTI_TOOL").get(null);
        isImmerseLoaded = true;
      } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
        isImmerseLoaded = false;
      }
    }

    @Override
    protected void addTables() {
    LootPool.Builder civilianLootPoolBuilder = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1));
        if (isImmerseLoaded) {
            LootPool.Builder immerseLootPoolBuilder = LootPool.lootPool()
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterCanteen).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseWaterCanteen).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterBottle).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedSweetcorn).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedBeans).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedTuna).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedPeaches).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedPasta).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedCornedBeef).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedCustard).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedPickles).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedDogFood).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedTomatoSoup).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedSweetcorn).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedBeans).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseScrewdriver).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedTuna).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedPeaches).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedPasta).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedCornedBeef).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedCustard).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedPickles).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedDogFood).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCannedTomatoSoup).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCanOpener).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterBottle).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immersePowerBar).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCandyBar).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCereal).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOrange).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRottenOrange).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immersePear).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRottenPear).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRiceBag).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseNoodles).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRottenMelonSlice).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseBlueberry).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRottenBlueberry).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRaspberry).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRottenRaspberry).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRanchChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCheesyChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseSaltedChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immersePopcorn).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseNuttyCereal).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmeraldCereal).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseFlakeCereal).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseMultiTool).get()).setWeight(5));
            civilianLootPoolBuilder.add(immerseLootPoolBuilder);
            }
        civilianLootPoolBuilder
        // Normal items
        .add(LootItem.lootTableItem(SurvivalItems.BLOODY_RAG.get()).setWeight(5))
        .add(LootItem.lootTableItem(SurvivalItems.DIRTY_RAG.get()).setWeight(5))
        .add(LootItem.lootTableItem(SurvivalItems.CLEAN_RAG.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.BEANIE_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BLUE_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BUNNY_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CLONE_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COOKIE_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COW_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CREEPER_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DEADPOOL_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DOCTOR_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FIREMAN_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.GREEN_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.HACKER_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.KNIGHT_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NINJA_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.ORANGE_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PAYDAY_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PAYDAY2_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PUMPKIN_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.RADAR_CAP.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SANTA_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SCUBA_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SHEEP_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SKI_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.TOP_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.TRAPPER_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.USHANKA_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.YELLOW_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.ZOMBIE_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SMART_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CASUAL_GREEN_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BUILDER_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BUSINESS_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SEC_GUARD_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CLONE_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COOKIE_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DEADPOOL_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NINJA_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PRESIDENT_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SCUBA_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.STANAG_20_ROUND_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.AK47_30_ROUND_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FNFAL_MAGAZINE.get()).setWeight(13))
        .add(LootItem.lootTableItem(ModItems.M1911_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.G18_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.M9_MAGAZINE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.DESERT_EAGLE_MAGAZINE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.P250_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.MAGNUM_AMMUNITION.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.FN57_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.MAC10_EXTENDED_MAGAZINE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.MAC10_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.SPORTER22_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.M1GARAND_AMMUNITION.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.TRENCH_GUN_SHELLS.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG_SHELLS.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.RED_DOT_SIGHT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.LP_SCOPE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.BIPOD.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.M1GARAND.get()).setWeight(6))
        .add(LootItem.lootTableItem(ModItems.SPORTER22.get()).setWeight(7))
        .add(LootItem.lootTableItem(ModItems.M1911.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.G18.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.M9.get()).setWeight(2))
        .add(LootItem.lootTableItem(ModItems.P250.get()).setWeight(8))
        .add(LootItem.lootTableItem(ModItems.MAGNUM.get()).setWeight(8))
        .add(LootItem.lootTableItem(ModItems.FN57.get()).setWeight(8))
        .add(LootItem.lootTableItem(ModItems.MAC10.get()).setWeight(8))
        .add(LootItem.lootTableItem(ModItems.TRENCH_GUN.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG.get()).setWeight(15))
        .add(LootItem.lootTableItem(SurvivalItems.PIPE_BOMB.get()).setWeight(3))
        .add(LootItem.lootTableItem(ModItems.CROWBAR.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.KATANA.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PIPE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.RUSTY_PIPE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FIRE_AXE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CHAINSAW.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BOWIE_KNIFE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.GOLF_CLUB.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NIGHT_STICK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SLEDGEHAMMER.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NAIL_BAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SHOVEL.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.HATCHET.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BROADSWORD.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MACHETE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.WEAPONIZED_SCYTHE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SCYTHE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PICKAXE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BO_STAFF.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.WRENCH.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FRYING_PAN.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BOLT_CUTTERS.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.KATANA.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COMBAT_KNIFE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.STEEL_BAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CLEAVER.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BROKEN_BOTTLE.get()).setWeight(5))
        .add(LootItem.lootTableItem(Items.BREAD).setWeight(15))
        .add(LootItem.lootTableItem(Items.APPLE).setWeight(15))
        .add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(15))
        .add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).setWeight(15))
        .add(LootItem.lootTableItem(Items.COOKIE).setWeight(15))
        .add(LootItem.lootTableItem(Items.MELON_SLICE).setWeight(15))
        .add(LootItem.lootTableItem(Items.CARROT).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.SMALL_RED_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SMALL_ORANGE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SMALL_YELLOW_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SMALL_GREEN_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SMALL_BLUE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SMALL_PURPLE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_RED_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_ORANGE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_YELLOW_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_GREEN_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_BLUE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_PURPLE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_GREY_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_BLACK_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_GHILLIE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MEDIUM_WHITE_BACKPACK.get()).setWeight(5))
        .add(LootItem.lootTableItem(Items.COOKED_CHICKEN).setWeight(15))   
        .add(LootItem.lootTableItem(Items.COOKED_BEEF).setWeight(15))
        .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(15));
        this.add(LootTables.CIVILIAN_LOOT_GENERATOR, LootTable.lootTable().withPool(civilianLootPoolBuilder));


    LootPool.Builder rareCivilianLootPoolBuilder = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1));
        if (isImmerseLoaded) {
            LootPool.Builder immerseLootPoolBuilder = LootPool.lootPool()
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedSweetcorn).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedBeans).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedTuna).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedPeaches).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedPasta).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedCornedBeef).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedCustard).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedPickles).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedDogFood).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseOpenCannedTomatoSoup).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCanOpener).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseMRE).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterBottle).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterCanteen).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseWaterCanteen).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseRanchChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseCheesyChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseSaltedChips).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immersePopcorn).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseNuttyCereal).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmeraldCereal).get()).setWeight(5))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseFlakeCereal).get()).setWeight(5));
            rareCivilianLootPoolBuilder.add(immerseLootPoolBuilder.build());
        }
    rareCivilianLootPoolBuilder
    // Normal items
        .add(LootItem.lootTableItem(SurvivalItems.BLOODY_RAG.get()).setWeight(20))
        .add(LootItem.lootTableItem(SurvivalItems.DIRTY_RAG.get()).setWeight(20))
        .add(LootItem.lootTableItem(SurvivalItems.CLEAN_RAG.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.FIRST_AID_KIT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BANDAGE.get()).setWeight(25))
        .add(LootItem.lootTableItem(SurvivalItems.SPLINT.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.RIOT_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BEANIE_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FIREMAN_CHIEF_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BLUE_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BUNNY_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CLONE_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COOKIE_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COW_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CREEPER_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DEADPOOL_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DOCTOR_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FIREMAN_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.GAS_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.GREEN_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.HACKER_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.KNIGHT_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NINJA_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.ORANGE_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PAYDAY_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PAYDAY2_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PUMPKIN_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.RADAR_CAP.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SANTA_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SCUBA_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SHEEP_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SKI_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.TOP_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.TRAPPER_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.USHANKA_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.YELLOW_HARD_HAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.ZOMBIE_MASK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CAMO_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SPACE_SUIT_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FIREMAN_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SMART_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CASUAL_GREEN_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BUILDER_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BUSINESS_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SEC_GUARD_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.RED_DUSK_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CLONE_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COOKIE_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DEADPOOL_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NINJA_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PRESIDENT_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.YELLOW_DUSK_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.ORANGE_DUSK_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.GREEN_DUSK_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.WHITE_DUSK_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PURPLE_DUSK_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SCUBA_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CONTRACTOR_CLOTHING.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.VULCAN_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.ASMO_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CANDY_APPLE_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CYREX_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DIAMOND_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.DRAGON_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FADE_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FURY_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.GEM_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.INFERNO_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.RUBY_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SCORCHED_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SLAUGHTER_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.UV_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.HYPER_BEAST_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.EMPEROR_DRAGON_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NUCLEAR_WINTER_PAINT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.STANAG_20_ROUND_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.STANAG_30_ROUND_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.AK47_30_ROUND_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.FNFAL_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.M1911_MAGAZINE.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.G18_MAGAZINE.get()).setWeight(50))
        .add(LootItem.lootTableItem(ModItems.M9_MAGAZINE.get()).setWeight(35))
        .add(LootItem.lootTableItem(ModItems.DESERT_EAGLE_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.P250_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.MAGNUM_AMMUNITION.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.FN57_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.MAC10_EXTENDED_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.MAC10_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.SPORTER22_MAGAZINE.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.M1GARAND_AMMUNITION.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.TRENCH_GUN_SHELLS.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG_SHELLS.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.RED_DOT_SIGHT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.LP_SCOPE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.HP_SCOPE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SUPPRESSOR.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.TACTICAL_GRIP.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.BIPOD.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.EOTECH_SIGHT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.M4A1.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.AK47.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FNFAL.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.M1GARAND.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SPORTER22.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.M1911.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.G18.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.M9.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.DESERT_EAGLE.get()).setWeight(35))
        .add(LootItem.lootTableItem(ModItems.P250.get()).setWeight(50))
        .add(LootItem.lootTableItem(ModItems.MAGNUM.get()).setWeight(75))
        .add(LootItem.lootTableItem(ModItems.FN57.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.MAC10.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.TRENCH_GUN.get()).setWeight(60))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG.get()).setWeight(60))
        .add(LootItem.lootTableItem(SurvivalItems.PIPE_BOMB.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.CROWBAR.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.KATANA.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PIPE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.RUSTY_PIPE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FIRE_AXE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CHAINSAW.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BOWIE_KNIFE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.GOLF_CLUB.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NIGHT_STICK.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SLEDGEHAMMER.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.NAIL_BAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SHOVEL.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.HATCHET.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BROADSWORD.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.MACHETE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.WEAPONIZED_SCYTHE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.SCYTHE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.PICKAXE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BO_STAFF.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.WRENCH.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.FRYING_PAN.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BOLT_CUTTERS.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.KATANA.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.COMBAT_KNIFE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.STEEL_BAT.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.CLEAVER.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BROKEN_BOTTLE.get()).setWeight(5))
        .add(LootItem.lootTableItem(Items.BREAD).setWeight(10))
        .add(LootItem.lootTableItem(Items.APPLE).setWeight(10))
        .add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(10))
        .add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).setWeight(10))
        .add(LootItem.lootTableItem(Items.COOKIE).setWeight(10))
        .add(LootItem.lootTableItem(Items.MELON_SLICE).setWeight(10))
        .add(LootItem.lootTableItem(Items.CARROT).setWeight(10))
        .add(LootItem.lootTableItem(Items.COOKED_CHICKEN).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BLACK_TACTICAL_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.GHILLIE_TACTICAL_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.GREEN_TACTICAL_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.GREY_TACTICAL_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.TAN_TACTICAL_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.LARGE_GREY_BACKPACK.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.LARGE_GREEN_BACKPACK.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.LARGE_TAN_BACKPACK.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.LARGE_BLACK_BACKPACK.get()).setWeight(10))
        .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(10));
    this.add(LootTables.RARE_CIVILIAN_LOOT_GENERATOR, LootTable.lootTable().withPool(rareCivilianLootPoolBuilder));


    LootPool.Builder medicalLootPoolBuilder = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1));
        if (isImmerseLoaded) {
            LootPool.Builder immerseLootPoolBuilder = LootPool.lootPool()
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterCanteen).get()).setWeight(15));
        medicalLootPoolBuilder.add(immerseLootPoolBuilder.build());
    }
    medicalLootPoolBuilder
    // Normal items
        .add(LootItem.lootTableItem(SurvivalItems.BLOODY_RAG.get()).setWeight(30))
        .add(LootItem.lootTableItem(SurvivalItems.DIRTY_RAG.get()).setWeight(30))
        .add(LootItem.lootTableItem(SurvivalItems.CLEAN_RAG.get()).setWeight(30))
        .add(LootItem.lootTableItem(SurvivalItems.SPLINT.get()).setWeight(40))
        .add(LootItem.lootTableItem(ModItems.FIRST_AID_KIT.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.ADRENALINE_SYRINGE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.SYRINGE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.BLOOD_SYRINGE.get()).setWeight(10))
        .add(LootItem.lootTableItem(SurvivalItems.RBI_SYRINGE.get()).setWeight(10))
        .add(LootItem.lootTableItem(SurvivalItems.CURE_SYRINGE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.BANDAGE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.DOCTOR_MASK.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.HAZMAT_HAT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.KNIGHT_HAT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.SCUBA_MASK.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.HAZMAT_CLOTHING.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.DOCTOR_CLOTHING.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.ARMY_MEDIC_CLOTHING.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.M9_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.P250_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.P90_MAGAZINE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.MP5A5_21_ROUND_MAGAZINE.get()).setWeight(6))
        .add(LootItem.lootTableItem(ModItems.MP5A5_35_ROUND_MAGAZINE.get()).setWeight(4))
        .add(LootItem.lootTableItem(ModItems.M9.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.P250.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.P90.get()).setWeight(8))
        .add(LootItem.lootTableItem(ModItems.MP5A5.get()).setWeight(8))
        .add(LootItem.lootTableItem(Items.BREAD).setWeight(10))
        .add(LootItem.lootTableItem(Items.APPLE).setWeight(10))
        .add(LootItem.lootTableItem(Items.MUSHROOM_STEW).setWeight(10))
        .add(LootItem.lootTableItem(Items.COOKED_PORKCHOP).setWeight(10))
        .add(LootItem.lootTableItem(Items.COOKIE).setWeight(10))
        .add(LootItem.lootTableItem(Items.MELON_SLICE).setWeight(10))
        .add(LootItem.lootTableItem(Items.CARROT).setWeight(10))
        .add(LootItem.lootTableItem(Items.COOKED_CHICKEN).setWeight(10))
        .add(LootItem.lootTableItem(Items.BAKED_POTATO).setWeight(10));
    this.add(LootTables.MEDICAL_LOOT_GENERATOR, LootTable.lootTable().withPool(medicalLootPoolBuilder));

    LootPool.Builder militaryLootPoolBuilder = LootPool.lootPool()
        .setRolls(ConstantValue.exactly(1));
        if (isImmerseLoaded) {
            LootPool.Builder immerseLootPoolBuilder = LootPool.lootPool()
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseFlakeCereal).get()).setWeight(10))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterCanteen).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseWaterCanteen).get()).setWeight(15));
            militaryLootPoolBuilder.add(immerseLootPoolBuilder.build());
        }
    militaryLootPoolBuilder
    // Normal items
        .add(LootItem.lootTableItem(ModItems.FIRST_AID_KIT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.ADRENALINE_SYRINGE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BANDAGE.get()).setWeight(10))
        .add(LootItem.lootTableItem(SurvivalItems.SPLINT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.BLACK_TACTICAL_VEST.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.GHILLIE_TACTICAL_VEST.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.GREEN_TACTICAL_VEST.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.GREY_TACTICAL_VEST.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.TAN_TACTICAL_VEST.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.LARGE_GREY_BACKPACK.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.LARGE_GREEN_BACKPACK.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.LARGE_TAN_BACKPACK.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.LARGE_BLACK_BACKPACK.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.ARMY_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.BLACK_BALLISTIC_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.CAMO_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.COMBAT_BDU_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.GAS_MASK.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.GHILLIE_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.GREEN_ARMY_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.GREEN_BALLISTIC_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.GREY_ARMY_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.JUGGERNAUT_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.KNIGHT_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.MILITARY_HAZMAT_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.NV_GOGGLES_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.PILOT_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SCUBA_MASK.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SPETSNAZ_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.CAMO_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.COMBAT_BDU_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.WINTER_ARMY_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.ARMY_DESERT_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.PILOT_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.TAC_GHILLIE_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.JUGGERNAUT_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.MIL_HAZMAT_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.FULL_GHILLIE_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.ARMY_MEDIC_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SCUBA_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.DDPAT_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.CONTRACTOR_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.VULCAN_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.ASMO_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.CANDY_APPLE_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.CYREX_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.DIAMOND_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FADE_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FURY_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.GEM_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.INFERNO_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.RUBY_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.SCORCHED_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.SLAUGHTER_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.UV_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.HYPER_BEAST_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.EMPEROR_DRAGON_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.NUCLEAR_WINTER_PAINT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.STANAG_BOX_MAGAZINE.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.STANAG_DRUM_MAGAZINE.get()).setWeight(6))
        .add(LootItem.lootTableItem(ModItems.MPT55_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.ACR_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.G36C_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.HK417_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.G18_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.M9_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.DESERT_EAGLE_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.P250_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.P90_MAGAZINE.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.MP5A5_35_ROUND_MAGAZINE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.MP5A5_21_ROUND_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.M107_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.AS50_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.AWP_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG_SHELLS.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.DMR_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.M240B_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.RPK_DRUM_MAGAZINE.get()).setWeight(6))
        .add(LootItem.lootTableItem(ModItems.RPK_MAGAZINE.get()).setWeight(7))
        .add(LootItem.lootTableItem(ModItems.MINIGUN_MAGAZINE.get()).setWeight(2))
        .add(LootItem.lootTableItem(ModItems.MK48MOD_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.RED_DOT_SIGHT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.ACOG_SIGHT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.LP_SCOPE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.HP_SCOPE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.SUPPRESSOR.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.TACTICAL_GRIP.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BIPOD.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.EOTECH_SIGHT.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.M4A1.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SCARL.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.ACR.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.HK417.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.MPT55.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.G36C.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.M240B.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.RPK.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.MINIGUN.get()).setWeight(1))
        .add(LootItem.lootTableItem(ModItems.MK48MOD.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.G18.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.M9.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.DESERT_EAGLE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.P250.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.P90.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.MP5A5.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.M107.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.AS50.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.AWP.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.DMR.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.FIRE_GRENADE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.SMOKE_GRENADE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.FLASH_GRENADE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.DECOY_GRENADE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.FRAG_GRENADE.get()).setWeight(10))
        .add(LootItem.lootTableItem(SurvivalItems.PIPE_BOMB.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.REMOTE_DETONATOR.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.CROWBAR.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BOWIE_KNIFE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.SLEDGEHAMMER.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.SHOVEL.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BOLT_CUTTERS.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.COMBAT_KNIFE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.PARACHUTE.get()).setWeight(10));
    this.add(LootTables.MILITARY_LOOT_GENERATOR, LootTable.lootTable().withPool(militaryLootPoolBuilder));


    LootPool.Builder policeLootPoolBuilder = LootPool.lootPool()
    .setRolls(ConstantValue.exactly(1));
        if (isImmerseLoaded) {
            LootPool.Builder immerseLootPoolBuilder = LootPool.lootPool()
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseMRE).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseEmptyWaterCanteen).get()).setWeight(15))
            .add(LootItem.lootTableItem(((RegistryObject<Item>) immerseWaterCanteen).get()).setWeight(15));
            policeLootPoolBuilder.add(immerseLootPoolBuilder.build());
        }
    policeLootPoolBuilder
    // Normal items
        .add(LootItem.lootTableItem(ModItems.FIRST_AID_KIT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.BANDAGE.get()).setWeight(20))
        .add(LootItem.lootTableItem(SurvivalItems.SPLINT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.BLACK_TACTICAL_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.GREY_TACTICAL_VEST.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.RIOT_VEST.get()).setWeight(5))
        .add(LootItem.lootTableItem(ModItems.BLACK_BALLISTIC_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.FIREMAN_CHIEF_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.FIREMAN_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.GAS_MASK.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.GREY_ARMY_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.JUGGERNAUT_HELMET.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.KNIGHT_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.NV_GOGGLES_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.RIOT_HAT.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SCUBA_MASK.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SAS_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.POLICE_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SWAT_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SHERIFF_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.JUGGERNAUT_CLOTHING.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.FIREMAN_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.BUSINESS_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SEC_GUARD_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.SCUBA_CLOTHING.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.STANAG_30_ROUND_MAGAZINE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.STANAG_20_ROUND_MAGAZINE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.FNFAL_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.G36C_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.G18_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.M9_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.P250_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.MAGNUM_AMMUNITION.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.FN57_MAGAZINE.get()).setWeight(30))
        .add(LootItem.lootTableItem(ModItems.P90_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.VECTOR_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.MP5A5_35_ROUND_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.MP5A5_21_ROUND_MAGAZINE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.M107_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.AS50_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.AWP_MAGAZINE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG_SHELLS.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.TASER_CARTRIDGE.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.RED_DOT_SIGHT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.ACOG_SIGHT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.LP_SCOPE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.HP_SCOPE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.SUPPRESSOR.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.TACTICAL_GRIP.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.BIPOD.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.EOTECH_SIGHT.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.M4A1.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FNFAL.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.G36C.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.TASER.get()).setWeight(20))
        .add(LootItem.lootTableItem(ModItems.G18.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.M9.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.P250.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.MAGNUM.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.FN57.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.P90.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.VECTOR.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.MP5A5.get()).setWeight(25))
        .add(LootItem.lootTableItem(ModItems.M107.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.AS50.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.AWP.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.MOSSBERG.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FIRE_GRENADE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.SMOKE_GRENADE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FLASH_GRENADE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.DECOY_GRENADE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.FRAG_GRENADE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.C4_EXPLOSIVE.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.REMOTE_DETONATOR.get()).setWeight(15))
        .add(LootItem.lootTableItem(ModItems.CROWBAR.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.FIRE_AXE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.NIGHT_STICK.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.SLEDGEHAMMER.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.BOLT_CUTTERS.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.COMBAT_KNIFE.get()).setWeight(10))
        .add(LootItem.lootTableItem(ModItems.PARACHUTE.get()).setWeight(25));
    this.add(LootTables.POLICE_LOOT_GENERATOR, LootTable.lootTable().withPool(policeLootPoolBuilder));


}
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return SurvivalBlocks.deferredRegister.getEntries().stream()
         .map(RegistryObject::get)
            .collect(Collectors.toSet());
    }
}