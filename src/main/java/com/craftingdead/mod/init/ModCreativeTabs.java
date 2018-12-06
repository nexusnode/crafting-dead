package com.craftingdead.mod.init;

import com.craftingdead.mod.CraftingDead;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {

	public static final CreativeTabs CRAFTING_DEAD = (new CreativeTabs(CraftingDead.MOD_ID) {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModBlocks.RESIDENTIAL_LOOT);
		}

	});

}
