package com.craftingdead.decoration.world.level.block;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
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

public class SecurityCameraBlock extends HorizontalDirectionalBlock {

  private static final Map<Direction, VoxelShape> shapes = Map.of(
      Direction.NORTH, Block.box(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D),
      Direction.SOUTH, Block.box(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D),
      Direction.WEST, Block.box(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D),
      Direction.EAST, Block.box(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D));

  protected SecurityCameraBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos,
      CollisionContext context) {
    return getShape(state);
  }

  public static VoxelShape getShape(BlockState state) {
    return shapes.get(state.getValue(FACING));
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
    var direction = state.getValue(FACING);
    var wallPos = pos.relative(direction.getOpposite());
    var wallState = level.getBlockState(wallPos);
    return wallState.isFaceSturdy(level, wallPos, direction);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var state = this.defaultBlockState();
    var level = context.getLevel();
    var pos = context.getClickedPos();
    var directions = context.getNearestLookingDirections();

    for (var direction : directions) {
      if (direction.getAxis().isHorizontal()) {
        var opposite = direction.getOpposite();
        state = state.setValue(FACING, opposite);
        if (state.canSurvive(level, pos)) {
          return state;
        }
      }
    }

    return null;
  }

  @Override
  public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
    return direction.getOpposite() == state.getValue(FACING)
        && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
  }
}
