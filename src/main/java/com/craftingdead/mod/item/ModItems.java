package com.craftingdead.mod.item;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.block.ModBlocks;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.util.ModSoundEvents;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CraftingDead.ID)
@Mod.EventBusSubscriber(modid = CraftingDead.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

  public static final Item ACR = null;
  public static final Item AK47 = null;
  public static final Item DESERT_EAGLE = null;
  public static final Item M4A1 = null;
  public static final Item M9 = null;
  public static final Item TASER = null;
  public static final Item MAGNUM = null;
  public static final Item FN57 = null;

  public static final Item RESIDENTIAL_LOOT = null;

  public static final Item CLIP = null;

  public static final Item EMPTY_WATER_BOTTLE = null;
  public static final Item WATER_BOTTLE = null;

  public static final Item POWER_BAR = null;
  public static final Item CANDY_BAR = null;
  public static final Item CEREAL = null;

  public static final Item CAN_OPENER = null;
  public static final Item SCREWDRIVER = null;
  public static final Item MULTI_TOOL = null;

  public static final Item CANNED_CORN = null;
  public static final Item OPEN_CANNED_CORN = null;
  public static final Item CANNED_BEANS = null;
  public static final Item OPEN_CANNED_BEANS = null;
  public static final Item CANNED_TUNA = null;
  public static final Item OPEN_CANNED_TUNA = null;
  public static final Item CANNED_PEACH = null;
  public static final Item OPEN_CANNED_PEACH = null;
  public static final Item CANNED_PASTA = null;
  public static final Item OPEN_CANNED_PASTA = null;
  public static final Item CANNED_BACON = null;
  public static final Item OPEN_CANNED_BACON = null;
  public static final Item CANNED_CUSTARD = null;
  public static final Item OPEN_CANNED_CUSTARD = null;
  public static final Item CANNED_PICKLES = null;
  public static final Item OPEN_CANNED_PICKLES = null;
  public static final Item CANNED_DOG_FOOD = null;
  public static final Item OPEN_CANNED_DOG_FOOD = null;
  public static final Item CANNED_TOMATO_SOUP = null;
  public static final Item OPEN_CANNED_TOMATO_SOUP = null;

  @SubscribeEvent
  public static void handle(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();

    registry.register(appendRegistryName("desert_eagle", new GunItem(new GunItem.Properties() //
        .setFireRate(0) //
        .setClipSize(0) //
        .setDamage(8) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.7F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.DESERT_EAGLE_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
    ));

    registry.register(appendRegistryName("acr", new GunItem(new GunItem.Properties() //
        .setFireRate(80) //
        .setClipSize(0) //
        .setDamage(7) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.8F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.ACR_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
    ));


    registry.register(appendRegistryName("ak47", new GunItem(new GunItem.Properties() //
        .setFireRate(80) //
        .setClipSize(0) //
        .setDamage(7) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.8F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.AK47_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
    ));

    registry.register(appendRegistryName("m4a1", new GunItem(new GunItem.Properties() //
        .setFireRate(80) //
        .setClipSize(0) //
        .setDamage(7) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.9F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.M4A1_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
    ));

    registry.register(appendRegistryName("m9", new GunItem(new GunItem.Properties() //
        .setFireRate(80) //
        .setClipSize(0) //
        .setDamage(7) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.9F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.M9_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
    ));

    registry.register(appendRegistryName("taser", new GunItem(new GunItem.Properties() //
        .setFireRate(2000) //
        .setClipSize(0) //
        .setDamage(7) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.9F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.TASER_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
    ));

    registry.register(appendRegistryName("magnum", new GunItem(new GunItem.Properties() //
        .setFireRate(80) //
        .setClipSize(0) //
        .setDamage(7) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.9F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.MAGNUM_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
    ));

    registry.register(appendRegistryName("fn57", new GunItem(new GunItem.Properties() //
        .setFireRate(80) //
        .setClipSize(0) //
        .setDamage(7) //
        .setReloadTime(2.2F) //
        .setAccuracy(0.9F) //
        .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
        .setShootSound(() -> ModSoundEvents.FN57_SHOOT) //
        .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
    ));

    registry.register(appendRegistryName("residential_loot",
        new BlockItem(ModBlocks.RESIDENTIAL_LOOT, (new Item.Properties())) //
    ));

    registry.register(appendRegistryName("empty_water_bottle", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("water_bottle",
        new DrinkItem(8, () -> EMPTY_WATER_BOTTLE, new Item.Properties() //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("power_bar", new Item(new Item.Properties() //
        .food(ModFoods.POWER_BAR) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("candy_bar", new Item(new Item.Properties() //
        .food(ModFoods.CANDY_BAR) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("cereal", new Item(new Item.Properties() //
        .food(ModFoods.CEREAL) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("can_opener", new ToolItem(new Item.Properties() //
        .maxDamage(8)) //
    ));

    registry.register(appendRegistryName("screwdriver", new ToolItem(new Item.Properties() //
        .maxDamage(4)) //
    ));

    registry.register(
        appendRegistryName("multi_tool", new MeleeWeaponItem(8, -2.4F, new Item.Properties() //
            .maxDamage(20)) //
        ));

    registry.register(appendRegistryName("canned_corn", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_corn", new Item(new Item.Properties() //
        .food(ModFoods.CANNED_CORN) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_beans", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_beans", new Item(new Item.Properties() //
        .food(ModFoods.CANNED_BEANS) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_tuna", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_tuna", new Item(new Item.Properties() //
        .food(ModFoods.CANNED_TUNA) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_peach", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_peach",
        new DrinkItem(2, null, new Item.Properties() //
            .food(ModFoods.CANNED_PEACH) //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_pasta", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_pasta", new Item(new Item.Properties() //
        .food(ModFoods.CANNED_PASTA) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_bacon", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_bacon", new Item(new Item.Properties() //
        .food(ModFoods.CANNED_BACON) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_custard", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_custard",
        new DrinkItem(4, null, new Item.Properties() //
            .food(ModFoods.CANNED_CUSTARD) //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_pickles", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_pickles", new Item(new Item.Properties() //
        .food(ModFoods.CANNED_PICKLES) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_dog_food", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_dog_food", new Item(new Item.Properties() //
        .food(ModFoods.CANNED_DOG_FOOD) //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("canned_tomato_soup", new Item(new Item.Properties() //
        .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));

    registry.register(appendRegistryName("open_canned_tomato_soup",
        new DrinkItem(3, null, new Item.Properties() //
            .food(ModFoods.CANNED_TOMATO_SOUP) //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
    ));
  }

  private static Item appendRegistryName(String registryName, Item item) {
    return item.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName));
  }
}
