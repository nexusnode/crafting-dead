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

package com.craftingdead.core.world.action.item;

import java.util.Optional;
import java.util.function.Predicate;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class BlockItemActionType extends ItemActionType<BlockItemAction> {

  private final Predicate<BlockState> predicate;

  protected BlockItemActionType(Builder builder) {
    super(builder);
    this.predicate = builder.predicate;
  }

  public Predicate<BlockState> getPredicate() {
    return this.predicate;
  }

  @Override
  public void encode(BlockItemAction action, FriendlyByteBuf out) {
    out.writeEnum(action.getHand());
    out.writeBlockHitResult(action.getContext().getHitResult());
  }

  @Override
  public BlockItemAction decode(LivingExtension<?, ?> performer, FriendlyByteBuf in) {
    return new BlockItemAction(in.readEnum(InteractionHand.class), this, performer,
        in.readBlockHitResult());
  }

  @Override
  public Optional<Action> createBlockAction(LivingExtension<?, ?> performer, UseOnContext context) {
    return Optional.of(new BlockItemAction(this, performer, context));
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends ItemActionType.Builder<Builder> {

    private Predicate<BlockState> predicate;

    public Builder forBlock(Predicate<BlockState> predicate) {
      this.predicate = predicate;
      return this;
    }

    @Override
    public BlockItemActionType build() {
      return new BlockItemActionType(this);
    }
  }
}
