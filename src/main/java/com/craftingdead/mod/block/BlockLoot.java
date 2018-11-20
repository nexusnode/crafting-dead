package com.craftingdead.mod.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.craftingdead.mod.inventory.CreativeTabs;
import com.craftingdead.mod.tileentity.TileEntityLoot;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLoot extends Block {

	private static final Random RANDOM = new Random();

	private final int colour;

	private final Map<ResourceLocation, Integer> itemToWeight;

	public BlockLoot(int colour, Map<ResourceLocation, Integer> itemToWeight) {
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabs.CRAFTING_DEAD);
		this.colour = colour;
		this.itemToWeight = itemToWeight;
	}

	public double getItemChance(ResourceLocation item) {
		int amount = 0;
		List<ResourceLocation> weightedItemList = this.getWeightedItemList();
		for (ResourceLocation weightedItem : weightedItemList)
			if (weightedItem.equals(item))
				amount++;
		return (amount / weightedItemList.size()) * 100;
	}

	private List<ResourceLocation> getWeightedItemList() {
		List<ResourceLocation> weightedItemsList = new ArrayList<ResourceLocation>();
		for (Entry<ResourceLocation, Integer> itemWeight : itemToWeight.entrySet()) {
			for (int i = 0; i < itemWeight.getValue(); i++) {
				weightedItemsList.add(itemWeight.getKey());
			}
		}
		return weightedItemsList;
	}

	public ResourceLocation getRandomItem() {
		List<ResourceLocation> weightedItemList = this.getWeightedItemList();
		return weightedItemList.get(RANDOM.nextInt(weightedItemList.size()));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			Item item = Item.REGISTRY.getObject(this.getRandomItem());
			if (item != null) {
				EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
						new ItemStack(item));
				world.spawnEntity(entityItem);
				world.removeTileEntity(pos);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntityLoot createTileEntity(World world, IBlockState state) {
		return new TileEntityLoot();
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	public int getColour() {
		return this.colour;
	}

}
