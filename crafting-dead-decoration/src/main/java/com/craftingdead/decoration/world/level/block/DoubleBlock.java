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

import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;

public class DoubleBlock extends HorizontalDirectionalBlock {

  public static final Property<Part> PART = EnumProperty.create("part", Part.class);

  protected DoubleBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(PART, Part.LEFT));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
      LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
    if (direction == getNeighbourDirection(state.getValue(PART), state.getValue(FACING))) {
      return neighborState.is(this) && neighborState.getValue(PART) != state.getValue(PART)
          ? state
          : Blocks.AIR.defaultBlockState();
    }
    return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
  }

  private static Direction getNeighbourDirection(Part part, Direction direction) {
    return part == Part.LEFT ? direction : direction.getOpposite();
  }

  @Override
  public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    if (!level.isClientSide() && player.isCreative()) {
      var part = state.getValue(PART);
      if (part == Part.LEFT) {
        var neighborPos =
            pos.relative(getNeighbourDirection(part, state.getValue(FACING)));
        var neighborState = level.getBlockState(neighborPos);
        if (neighborState.is(this) && neighborState.getValue(PART) == Part.RIGHT) {
          level.setBlock(neighborPos, Blocks.AIR.defaultBlockState(),
              Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
          level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, neighborPos,
              Block.getId(neighborState));
        }
      }
    }

    super.playerWillDestroy(level, pos, state, player);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    var direction = context.getHorizontalDirection();
    var pos = context.getClickedPos();
    var otherPos = pos.relative(direction);
    var level = context.getLevel();
    return level.getBlockState(otherPos).canBeReplaced(context)
        && level.getWorldBorder().isWithinBounds(otherPos)
            ? this.defaultBlockState().setValue(FACING, direction)
            : null;
  }

  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, PART);
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state,
      @Nullable LivingEntity entity, ItemStack item) {
    super.setPlacedBy(level, pos, state, entity, item);
    if (!level.isClientSide()) {
      var otherPos = pos.relative(state.getValue(FACING));
      level.setBlock(otherPos, state.setValue(PART, Part.RIGHT),
          Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);
      level.blockUpdated(pos, Blocks.AIR);
      state.updateNeighbourShapes(level, pos, Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);
    }
  }

  public static Direction getConnectedDirection(BlockState state) {
    var facing = state.getValue(FACING);
    return state.getValue(PART) == Part.LEFT ? facing.getOpposite() : facing;
  }

  public static DoubleBlockCombiner.BlockType getBlockType(BlockState state) {
    return state.getValue(PART) == Part.LEFT
        ? DoubleBlockCombiner.BlockType.FIRST
        : DoubleBlockCombiner.BlockType.SECOND;
  }


  public enum Part implements StringRepresentable {

    LEFT("left"),
    RIGHT("right");

    private final String name;

    private Part(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
