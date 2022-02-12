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
