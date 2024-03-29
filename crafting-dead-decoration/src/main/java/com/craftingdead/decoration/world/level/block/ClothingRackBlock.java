/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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
