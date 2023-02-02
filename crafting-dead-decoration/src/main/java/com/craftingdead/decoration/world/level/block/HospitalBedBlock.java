package com.craftingdead.decoration.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HospitalBedBlock extends DoubleBlock {

  protected static final int HEIGHT = 9;
  protected static final VoxelShape BASE = Block.box(0.0D, 3.0D, 0.0D, 16.0D, HEIGHT, 16.0D);
  private static final int LEG_WIDTH = 3;
  protected static final VoxelShape LEG_NORTH_WEST =
      Block.box(0.0D, 0.0D, 0.0D, LEG_WIDTH, LEG_WIDTH, LEG_WIDTH);
  protected static final VoxelShape LEG_SOUTH_WEST =
      Block.box(0.0D, 0.0D, 16.0D - LEG_WIDTH, LEG_WIDTH, LEG_WIDTH, 16.0D);
  protected static final VoxelShape LEG_NORTH_EAST =
      Block.box(16.0D - LEG_WIDTH, 0.0D, 0.0D, 16.0D, LEG_WIDTH, LEG_WIDTH);
  protected static final VoxelShape LEG_SOUTH_EAST =
      Block.box(16.0D - LEG_WIDTH, 0.0D, 16.0D - LEG_WIDTH, 16.0D, LEG_WIDTH, 16.0D);
  protected static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, LEG_NORTH_WEST, LEG_NORTH_EAST);
  protected static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, LEG_SOUTH_WEST, LEG_SOUTH_EAST);
  protected static final VoxelShape WEST_SHAPE = Shapes.or(BASE, LEG_NORTH_WEST, LEG_SOUTH_WEST);
  protected static final VoxelShape EAST_SHAPE = Shapes.or(BASE, LEG_NORTH_EAST, LEG_SOUTH_EAST);

  protected HospitalBedBlock(Properties properties) {
    super(properties);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter getter,
      BlockPos pos, CollisionContext context) {
    var direction = getConnectedDirection(state).getOpposite();
    return switch (direction) {
      case NORTH -> NORTH_SHAPE;
      case SOUTH -> SOUTH_SHAPE;
      case WEST -> WEST_SHAPE;
      default -> EAST_SHAPE;
    };
  }
}
