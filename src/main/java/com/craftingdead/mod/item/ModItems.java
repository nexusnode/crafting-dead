package com.craftingdead.mod.item;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.block.ModBlocks;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.potion.ModEffects;
import com.craftingdead.mod.util.ModSoundEvents;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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

  @SubscribeEvent
  public static void handle(RegistryEvent.Register<Item> event) {
    event.getRegistry()
        .registerAll(appendRegistryName("desert_eagle", new GunItem(new GunItem.Properties() //
            .setFireRate(0) //
            .setClipSize(0) //
            .setDamage(8) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.7F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.DESERT_EAGLE_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
        ), appendRegistryName("acr", new GunItem(new GunItem.Properties() //
            .setFireRate(80) //
            .setClipSize(0) //
            .setDamage(7) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.8F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.ACR_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
        ), appendRegistryName("ak47", new GunItem(new GunItem.Properties() //
            .setFireRate(80) //
            .setClipSize(0) //
            .setDamage(7) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.8F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.AK47_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
        ), appendRegistryName("m4a1", new GunItem(new GunItem.Properties() //
            .setFireRate(80) //
            .setClipSize(0) //
            .setDamage(7) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.9F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.AUTO, IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.M4A1_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
        ), appendRegistryName("m9", new GunItem(new GunItem.Properties() //
            .setFireRate(80) //
            .setClipSize(0) //
            .setDamage(7) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.9F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.M9_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
        ), appendRegistryName("taser", new GunItem(new GunItem.Properties() //
            .setFireRate(2000) //
            .setClipSize(0) //
            .setDamage(7) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.9F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.TASER_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
        ), appendRegistryName("magnum", new GunItem(new GunItem.Properties() //
            .setFireRate(80) //
            .setClipSize(0) //
            .setDamage(7) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.9F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.MAGNUM_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
        ), appendRegistryName("fn57", new GunItem(new GunItem.Properties() //
            .setFireRate(80) //
            .setClipSize(0) //
            .setDamage(7) //
            .setReloadTime(2.2F) //
            .setAccuracy(0.9F) //
            .setFireModes(ImmutableList.of(IFireMode.Modes.SEMI)) //
            .setShootSound(() -> ModSoundEvents.FN57_SHOOT) //
            .setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
        ), appendRegistryName("residential_loot",
            new BlockItem(ModBlocks.RESIDENTIAL_LOOT, (new Item.Properties())) //
        ), appendRegistryName("empty_water_bottle", new Item(new Item.Properties() //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
        ), appendRegistryName("water_bottle",
            new UsableItem((UsableItem.Properties) new UsableItem.Properties() //
                .addEffect(() -> new EffectInstance(ModEffects.HYDRATE, 0, 8)) //
                .setReturnItem(() -> EMPTY_WATER_BOTTLE) //
                .setUseAction(UseAction.DRINK).group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
        ), appendRegistryName("power_bar", new Item(new Item.Properties() //
            .food(ModFoods.POWER_BAR) //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
        ), appendRegistryName("candy_bar", new Item(new Item.Properties() //
            .food(ModFoods.CANDY_BAR) //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
        ), appendRegistryName("cereal", new Item(new Item.Properties() //
            .food(ModFoods.CEREAL) //
            .group(ModItemGroups.CRAFTING_DEAD_CONSUMABLE)) //
        ));
  }

  private static Item appendRegistryName(String registryName, Item item) {
    return item.setRegistryName(new ResourceLocation(CraftingDead.ID, registryName));
  }
}
