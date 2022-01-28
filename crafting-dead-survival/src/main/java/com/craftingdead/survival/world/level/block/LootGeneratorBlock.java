/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.survival.world.level.block;

import java.util.Random;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LootGeneratorBlock extends AirBlock {

  private final Supplier<Block> lootBlock;
  private final Supplier<? extends ParticleOptions> particleOptions;
  private final IntSupplier refreshDelayTicks;

  public LootGeneratorBlock(Properties properties, Supplier<Block> lootBlock,
      Supplier<? extends ParticleOptions> particleOptions, IntSupplier refreshDelayTicks) {
    super(properties);
    this.lootBlock = lootBlock;
    this.particleOptions = particleOptions;
    this.refreshDelayTicks = refreshDelayTicks;
  }

  @Override
  public RenderShape getRenderShape(BlockState blockState) {
    return RenderShape.INVISIBLE;
  }

  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos,
      CollisionContext context) {
    return Block.box(0, 0, 0, 16, 3, 16);
  }

  @Override
  public void animateTick(BlockState blockState, Level level, BlockPos pos, Random random) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.gameMode.getPlayerMode() == GameType.CREATIVE) {
      level.addParticle(this.particleOptions.get(), pos.getX() + 0.5D, pos.getY() + 0.5D,
          pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updateShape(BlockState blockState, Direction direction,
      BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
    level.scheduleTick(pos, this, 0);
    return super.updateShape(blockState, direction, neighborState, level, pos, neighborPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState blockState, ServerLevel level, BlockPos pos, Random random) {
    super.tick(blockState, level, pos, random);

    level.scheduleTick(pos, this, this.refreshDelayTicks.getAsInt());

    BlockState lootBlockState = this.lootBlock.get().defaultBlockState();

    boolean lootExists =
        level.getBlockStates(new AABB(pos.north().west(), pos.south().east()))
            .anyMatch(lootBlockState::equals);
    if (!lootExists) {
      if (level.isEmptyBlock(pos.north())) {
        level.setBlockAndUpdate(pos.north(), lootBlockState);
      } else if (level.isEmptyBlock(pos.east())) {
        level.setBlockAndUpdate(pos.east(), lootBlockState);
      } else if (level.isEmptyBlock(pos.south())) {
        level.setBlockAndUpdate(pos.south(), lootBlockState);
      } else if (level.isEmptyBlock(pos.west())) {
        level.setBlockAndUpdate(pos.west(), lootBlockState);
      }
    }
  }
}
