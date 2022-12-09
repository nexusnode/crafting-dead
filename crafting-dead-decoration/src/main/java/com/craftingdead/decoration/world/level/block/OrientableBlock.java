package com.craftingdead.decoration.world.level.block;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OrientableBlock extends HorizontalDirectionalBlock {

  private final Function<Direction, VoxelShape> shapes;
  private final boolean wallMounted;

  public OrientableBlock(Properties properties, Function<Direction, VoxelShape> shapes) {
    this(properties, shapes, false);
  }

  public OrientableBlock(Properties properties, Function<Direction, VoxelShape> shapes,
      boolean wallMounted) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH));
    this.shapes = shapes;
    this.wallMounted = wallMounted;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    if (!this.wallMounted) {
      return this.defaultBlockState()
          .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    var state = this.defaultBlockState();
    for (var direction : context.getNearestLookingDirections()) {
      if (direction.getAxis().isHorizontal()) {
        var opposite = direction.getOpposite();
        state = state.setValue(FACING, opposite);
        if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
          return state;
        }
      }
    }
    return null;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter getter,
      BlockPos pos, CollisionContext context) {
    return this.shapes.apply(state.getValue(HorizontalDirectionalBlock.FACING));
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    if (!this.wallMounted) {
      return true;
    }

    var direction = state.getValue(FACING);
    var wallPos = pos.relative(direction.getOpposite());
    var wallState = level.getBlockState(wallPos);
    return wallState.isFaceSturdy(level, wallPos, direction);
  }

  @Override
  public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
    return this.wallMounted
        && direction.getOpposite() == state.getValue(FACING)
        && !state.canSurvive(level, pos)
            ? Blocks.AIR.defaultBlockState()
            : state;
  }
}
