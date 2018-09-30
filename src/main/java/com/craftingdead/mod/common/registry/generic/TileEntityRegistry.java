package com.craftingdead.mod.common.registry.generic;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.tileentity.TileEntityLoot;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegistry {

	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityLoot.class, new ResourceLocation(CraftingDead.MOD_ID, "loot"));
	}

}
