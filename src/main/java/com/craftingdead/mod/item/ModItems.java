package com.craftingdead.mod.item;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.block.ModBlocks;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.util.ModSoundEvents;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

	@SubscribeEvent
	public static void handle(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll( //
				appendRegistryName("desert_eagle", new GunItem(new GunItem.Properties() //
						.setFireRate(0) //
						.setClipSize(0) //
						.setDamage(8) //
						.setReloadTime(2.2F) //
						.setSpread(3F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.DESERT_EAGLE_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
				), appendRegistryName("acr", new GunItem(new GunItem.Properties() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(2F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.AUTO, FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.ACR_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
				), appendRegistryName("ak47", new GunItem(new GunItem.Properties() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(2F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.AUTO, FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.AK47_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
				), appendRegistryName("m4a1", new GunItem(new GunItem.Properties() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1.5F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.AUTO, FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.M4A1_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new))) //
				), appendRegistryName("m9", new GunItem(new GunItem.Properties() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.M9_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
				), appendRegistryName("taser", new GunItem(new GunItem.Properties() //
						.setFireRate(2000) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.TASER_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
				), appendRegistryName("magnum", new GunItem(new GunItem.Properties() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.MAGNUM_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
				), appendRegistryName("fn57", new GunItem(new GunItem.Properties() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.FN57_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new))) //
				), appendRegistryName("residential_loot",
						createBlockItem(ModBlocks.RESIDENTIAL_LOOT, ModItemGroups.CRAFTING_DEAD)) //
		);
	}

	private static Item appendRegistryName(String registryName, Item item) {
		return item.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName));
	}

	private static Item createBlockItem(Block block, ItemGroup itemGroup) {
		return new BlockItem(block, (new Item.Properties()).group(itemGroup));
	}
}
