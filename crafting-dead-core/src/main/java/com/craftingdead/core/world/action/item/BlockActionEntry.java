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

import java.util.function.Predicate;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.ForgeMod;

public final class BlockActionEntry extends AbstractActionEntry {

  private final Predicate<BlockState> predicate;

  private BlockPos blockPosTarget;
  private BlockState blockStateTarget;

  private BlockActionEntry(Builder builder) {
    super(builder);
    this.predicate = builder.predicate;
  }

  @Override
  public boolean canPerform(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target,
      ItemStack heldStack) {
    ModifiableAttributeInstance reachDistanceAttribute =
        performer.getEntity().getAttribute(ForgeMod.REACH_DISTANCE.get());
    RayTraceResult result = performer.getEntity().pick(
        reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue(), 1.0F, true);
    if (result instanceof BlockRayTraceResult) {
      BlockPos blockPos = ((BlockRayTraceResult) result).getBlockPos();
      BlockState blockState = performer.getLevel().getBlockState(blockPos);
      if (this.blockPosTarget == null || this.blockStateTarget == null) {
        this.blockPosTarget = blockPos;
        this.blockStateTarget = blockState;
        if (!this.predicate.test(this.blockStateTarget)) {
          return false;
        }
      }

      return this.blockPosTarget.equals(blockPos) && this.blockStateTarget.equals(blockState);
    }
    return false;
  }

  @Override
  public boolean finish(LivingExtension<?, ?> performer, LivingExtension<?, ?> target,
      ItemStack heldStack) {
    return true;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends AbstractActionEntry.Builder<Builder> {

    private Predicate<BlockState> predicate;

    private Builder() {
      super(BlockActionEntry::new);
    }

    public Builder setPredicate(Predicate<BlockState> predicate) {
      this.predicate = predicate;
      return this;
    }
  }
}
