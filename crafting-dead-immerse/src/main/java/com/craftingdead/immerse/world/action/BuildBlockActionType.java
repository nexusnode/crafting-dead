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

import java.util.function.Supplier;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class BuildBlockActionType extends BlueprintActionType {

  private final Supplier<Block> block;

  protected BuildBlockActionType(Builder builder) {
    super(builder);
    this.block = builder.block;
  }

  public Block getBlock() {
    return this.block.get();
  }

  @Override
  protected BlueprintAction create(LivingExtension<?, ?> performer, BlockPlaceContext context) {
    return new BuildBlockAction(performer, context, this);
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends BlueprintActionType.Builder<Builder> {

    private Supplier<Block> block;

    public Builder block(Block block) {
      return this.block(() -> block);
    }

    public Builder block(Supplier<Block> block) {
      this.block = block;
      return this;
    }

    @Override
    public ItemActionType<?> build() {
      return new BuildBlockActionType(this);
    }
  }
}
