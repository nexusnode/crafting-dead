package com.craftingdead.mod.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LootBlock extends Block {

	private static final Random RANDOM = new Random();

	private final int colour;

	private final Map<ResourceLocation, Integer> itemToWeight;

	public LootBlock(int colour, Map<ResourceLocation, Integer> itemToWeight) {
		super(Block.Properties.create(Material.ROCK).hardnessAndResistance(-1F));
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
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult hit) {
		if (!world.isRemote) {
			Item item = GameRegistry.findRegistry(Item.class).getValue(this.getRandomItem());
			if (item != null) {
				ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
						new ItemStack(item));
				world.func_217376_c(entityItem);
				world.removeTileEntity(pos);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	public int getColour() {
		return this.colour;
	}
}
