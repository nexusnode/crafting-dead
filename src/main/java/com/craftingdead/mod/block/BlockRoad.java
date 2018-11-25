package com.craftingdead.mod.block;

import javax.annotation.Nullable;

import com.craftingdead.mod.init.ModCreativeTabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockRoad extends Block {

	public BlockRoad() {
		super(Material.ROCK);
		this.setCreativeTab(ModCreativeTabs.CRAFTING_DEAD_DECORATIVE);
	}

	@Override
	public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity entity) {
		return 1.1F;
	}

}
