package com.craftingdead.decoration.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapedBlock extends Block {

  private final VoxelShape shape;

  public ShapedBlock(Properties properties, VoxelShape shape) {
    super(properties);
    this.shape = shape;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter getter,
      BlockPos pos, CollisionContext context) {
    return this.shape;
  }
}
