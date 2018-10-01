package com.craftingdead.mod.client.renderer.color;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BasicColourHandler implements IBlockColor, IItemColor {

	private final int colour;

	public BasicColourHandler(int colour) {
		this.colour = colour;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		return colour;
	}

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		return colour;
	}

}