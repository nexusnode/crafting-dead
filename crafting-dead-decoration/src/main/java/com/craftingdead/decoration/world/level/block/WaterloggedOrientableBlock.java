package com.craftingdead.decoration.world.level.block;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WaterloggedOrientableBlock extends OrientableBlock implements SimpleWaterloggedBlock {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public WaterloggedOrientableBlock(Properties properties, Function<Direction, VoxelShape> shapes) {
    this(properties, shapes, false);
  }

  public WaterloggedOrientableBlock(Properties properties, Function<Direction, VoxelShape> shapes,
      boolean wallMounted) {
    super(properties, shapes, wallMounted);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var result = super.getStateForPlacement(context);
    if (result == null) {
      return null;
    }
    var fluidState = context.getLevel().getFluidState(context.getClickedPos());
    return result.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  @Override
  public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos pos) {
    return !state.getValue(WATERLOGGED);
  }
}
