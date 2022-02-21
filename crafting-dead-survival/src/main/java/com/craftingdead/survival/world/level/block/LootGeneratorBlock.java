/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.survival.world.level.block;

import java.util.Random;
import java.util.function.BooleanSupplier;
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
  private final BooleanSupplier enabled;

  public LootGeneratorBlock(Properties properties, Supplier<Block> lootBlock,
      Supplier<? extends ParticleOptions> particleOptions, IntSupplier refreshDelayTicks,
      BooleanSupplier enabled) {
    super(properties);
    this.lootBlock = lootBlock;
    this.particleOptions = particleOptions;
    this.refreshDelayTicks = refreshDelayTicks;
    this.enabled = enabled;
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
    if (!this.enabled.getAsBoolean()) {
      return;
    }

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
