package com.craftingdead.mod.common.item;

import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class CustomItem extends Item {

	public CustomItem(String registryName) {
		this.setRegistryName(new ResourceLocation(CraftingDead.MOD_ID, registryName));
		this.setTranslationKey(CraftingDead.MOD_ID + "." + registryName);
	}

}
