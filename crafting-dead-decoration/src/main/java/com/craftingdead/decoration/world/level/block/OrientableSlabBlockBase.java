package com.craftingdead.decoration.world.level.block;

import javax.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.SlabType;

public class OrientableSlabBlockBase extends SlabBlock {

  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

  public OrientableSlabBlockBase(Block.Properties properties) {
    super(properties);
    this.registerDefaultState(this.defaultBlockState()
        .setValue(TYPE, SlabType.BOTTOM)
        .setValue(WATERLOGGED, false)
        .setValue(FACING, Direction.NORTH));

  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var result = super.getStateForPlacement(context);
    return result == null
        ? null
        : result.setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  @NotNull
  public BlockState rotate(BlockState state, Rotation rot) {
    return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  @NotNull
  public BlockState mirror(BlockState state, Mirror mirrorIn) {
    return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState state) {
    return state.getValue(TYPE) != SlabType.DOUBLE;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(TYPE, WATERLOGGED, FACING);
  }
}
