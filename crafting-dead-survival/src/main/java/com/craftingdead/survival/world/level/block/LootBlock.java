package com.craftingdead.survival.world.level.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LootBlock extends Block {

  public LootBlock(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player,
      Hand handIn, BlockRayTraceResult hit) {
    level.destroyBlock(pos, true);
    return ActionResultType.SUCCESS;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos,
      ISelectionContext context) {
    return Block.box(0, 0, 0, 16, 3, 16);
  }
}
