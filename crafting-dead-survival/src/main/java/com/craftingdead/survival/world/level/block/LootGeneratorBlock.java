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
import java.util.function.Supplier;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.GameType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class LootGeneratorBlock extends AirBlock {

  private final Supplier<Block> lootBlock;
  private final Supplier<IParticleData> particleOptions;

  public LootGeneratorBlock(Properties properties, Supplier<Block> lootBlock,
      Supplier<IParticleData> particleOptions) {
    super(properties);
    this.lootBlock = lootBlock;
    this.particleOptions = particleOptions;
  }

  @Override
  public BlockRenderType getRenderShape(BlockState blockState) {
    return BlockRenderType.INVISIBLE;
  }

  @Override
  public VoxelShape getShape(BlockState blockState, IBlockReader level, BlockPos pos,
      ISelectionContext context) {
    return Block.box(0, 0, 0, 16, 3, 16);
  }

  @Override
  public void animateTick(BlockState blockState, World level, BlockPos pos, Random random) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.gameMode.getPlayerMode() == GameType.CREATIVE) {
      level.addParticle(this.particleOptions.get(), pos.getX() + 0.5D, pos.getY() + 0.5D,
          pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void randomTick(BlockState blockState, ServerWorld level, BlockPos pos, Random random) {
    super.randomTick(blockState, level, pos, random);

    BlockState lootBlockState = this.lootBlock.get().defaultBlockState();

    boolean lootExists =
        level.getBlockStates(new AxisAlignedBB(pos.north().west(), pos.south().east()))
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
