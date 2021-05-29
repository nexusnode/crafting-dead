package com.craftingdead.survival.world.level.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

public class LootGenerator extends Block {

  private final String lootType;

  public LootGenerator(Properties properties, String lootType) {
    super(properties.randomTicks().noOcclusion().noCollission());
    this.lootType = lootType;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
    switch (this.lootType) {
      case "military":
        worldIn.setBlockAndUpdate(pos, SurvivalBlocks.MILITARY_LOOT.get().defaultBlockState());
        break;
      case "medic":
        worldIn.setBlockAndUpdate(pos, SurvivalBlocks.MEDIC_LOOT.get().defaultBlockState());
        break;
      case "civilian":
        worldIn.setBlockAndUpdate(pos, SurvivalBlocks.CIVILIAN_LOOT.get().defaultBlockState());
        break;
      case "civilian_rare":
        worldIn.setBlockAndUpdate(pos, SurvivalBlocks.CIVILIAN_RARE_LOOT.get().defaultBlockState());
        break;
      case "police":
        worldIn.setBlockAndUpdate(pos, SurvivalBlocks.POLICE_LOOT.get().defaultBlockState());
        break;
    }
    super.randomTick(state, worldIn, pos, random);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos,
      ISelectionContext context) {
    return Block.box(0, 0, 0, 16, 3, 16);
  }
}
