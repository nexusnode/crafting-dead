package com.craftingdead.decoration.world.level.block;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClothingRackBlock extends DoubleBlock {

  private static final VoxelShape SHAPE = Shapes.or(
      box(14, 0, 14, 16, 4, 16),
      box(0, 0, 14, 2, 4, 16),
      box(2, 2, 14, 14, 4, 16),
      box(7.5, 27.5, 0, 8.5, 28.75, 16.75),
      box(7, 4, 14, 9, 29, 16),
      box(7, 2, 0, 9, 4, 14));
  private static final Function<Direction, VoxelShape> directionShapes =
      BlockShapes.rotatedOrientableShape(SHAPE);

  protected ClothingRackBlock(Properties properties) {
    super(properties);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter getter,
      BlockPos pos, CollisionContext context) {
    return directionShapes.apply(getConnectedDirection(state).getOpposite());
  }
}
