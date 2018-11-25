package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {

	public static final CreativeTabs CRAFTING_DEAD = (new CreativeTabs(CraftingDead.MOD_ID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.LINED_ROAD);
		}

	});

	public static final CreativeTabs CRAFTING_DEAD_DECORATIVE = (new CreativeTabs(getFormattedName("decorative")) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.BARBED_WIRE);
		}

	});

	private static String getFormattedName(String name) {
		return String.format("%s%s%s", CraftingDead.MOD_ID, ".", name);
	}

}
