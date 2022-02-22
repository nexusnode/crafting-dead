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

package com.craftingdead.immerse.world.action;

import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;

public class BuildCuboidActionType extends AbstractBuildBlockActionType {

  private final BlockPos minOffset;
  private final BlockPos maxOffset;

  protected BuildCuboidActionType(Builder builder) {
    super(builder);
    this.minOffset = builder.minOffset;
    this.maxOffset = builder.maxOffset;
  }

  public BlockPos getMinOffset() {
    return this.minOffset;
  }

  public BlockPos getMaxOffset() {
    return this.maxOffset;
  }

  @Override
  protected BuildAction create(LivingExtension<?, ?> performer, BlockPlaceContext context) {
    return new BuildCuboidAction(performer, context, this);
  }

  public static Builder block() {
    return new Builder(BlockPos.ZERO, BlockPos.ZERO);
  }

  public static Builder wall() {
    return new Builder(new BlockPos(-1, 0, 0), new BlockPos(1, 2, 0));
  }

  public static Builder platform() {
    return new Builder(new BlockPos(-1, 0, 0), new BlockPos(1, 0, 2));
  }

  public static Builder builder(BlockPos minOffset, BlockPos maxOffset) {
    return new Builder(minOffset, maxOffset);
  }

  public static class Builder extends AbstractBuildBlockActionType.Builder<Builder> {

    private final BlockPos minOffset;
    private final BlockPos maxOffset;

    private Builder(BlockPos minOffset, BlockPos maxOffset) {
      this.minOffset = minOffset;
      this.maxOffset = maxOffset;
    }

    @Override
    public ItemActionType<?> build() {
      return new BuildCuboidActionType(this);
    }
  }
}
