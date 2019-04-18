package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.client.animation.GunAnimation;
import com.craftingdead.mod.client.animation.fire.PistolShootAnimation;
import com.craftingdead.mod.client.animation.fire.RifleShootAnimation;
import com.craftingdead.mod.item.FireMode;
import com.craftingdead.mod.item.ItemGun;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(CraftingDead.MOD_ID)
@Mod.EventBusSubscriber
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
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll( //
				appendRegistryName("desert_eagle", new ItemGun.Builder() //
						.setFireRate(0) //
						.setClipSize(0) //
						.setDamage(8) //
						.setReloadTime(2.2F) //
						.setSpread(3F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.DESERT_EAGLE_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new)) //
						.build() //
				), appendRegistryName("acr", new ItemGun.Builder() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(2F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.AUTO, FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.ACR_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new)) //
						.build() //
				), appendRegistryName("ak47", new ItemGun.Builder() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(2F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.AUTO, FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.AK47_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new)) //
						.build() //
				), appendRegistryName("m4a1", new ItemGun.Builder() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1.5F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.AUTO, FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.M4A1_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, RifleShootAnimation::new)) //
						.build() //
				), appendRegistryName("m9", new ItemGun.Builder() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.M9_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new)) //
						.build() //
				), appendRegistryName("taser", new ItemGun.Builder() //
						.setFireRate(2000) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.TASER_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new)) //
						.build() //
				), appendRegistryName("magnum", new ItemGun.Builder() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.MAGNUM_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new)) //
						.build() //
				), appendRegistryName("fn57", new ItemGun.Builder() //
						.setFireRate(80) //
						.setClipSize(0) //
						.setDamage(7) //
						.setReloadTime(2.2F) //
						.setSpread(1F) //
						.setFireModes(ImmutableList.of(FireMode.Modes.SEMI)) //
						.setShootSound(() -> ModSoundEvents.FN57_SHOOT) //
						.setAnimations(ImmutableMap.of(GunAnimation.Type.SHOOT, PistolShootAnimation::new)) //
						.build() //
				), appendRegistryName("residential_loot", new ItemBlock(ModBlocks.RESIDENTIAL_LOOT) //
				), appendRegistryName("clip", new Item()));
	}

	private static Item appendRegistryName(String registryName, Item item) {
		return item.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName))
				.setTranslationKey(String.format("%s%s%s", CraftingDead.MOD_ID, ".", registryName));
	}

}
