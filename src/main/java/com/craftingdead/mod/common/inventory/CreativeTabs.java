package com.craftingdead.mod.common.inventory;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.registry.forge.BlockRegistry;

import net.minecraft.item.ItemStack;

public abstract class CreativeTabs extends net.minecraft.creativetab.CreativeTabs {

	public static final CreativeTabs CRAFTING_DEAD = (new CreativeTabs(CraftingDead.MOD_ID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(BlockRegistry.LINED_ROAD);
		}

	});

	public static final CreativeTabs CRAFTING_DEAD_DECORATIVE = (new CreativeTabs(getFormattedName("decorative")) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(BlockRegistry.BARBED_WIRE);
		}

	});

	public CreativeTabs(String label) {
		super(label);
	}

	public CreativeTabs(int index, String label) {
		super(index, label);
	}

	private static String getFormattedName(String name) {
		return String.format("%s%s%s", CraftingDead.MOD_ID, ".", name);
	}

}
