/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.world.action.delegate;

import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;

public final class DelegatedBlockAction extends AbstractDelegateAction<DelegateBlockActionType> {

  private BlockPos blockPosTarget;
  private BlockState blockStateTarget;

  DelegatedBlockAction(DelegateBlockActionType type) {
    super(type);
  }

  @Override
  public boolean canPerform(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target,
      ItemStack heldStack) {
    AttributeInstance reachDistanceAttribute =
        performer.getEntity().getAttribute(ForgeMod.REACH_DISTANCE.get());
    HitResult result = performer.getEntity().pick(
        reachDistanceAttribute == null ? 4.0D : reachDistanceAttribute.getValue(), 1.0F, true);
    if (result instanceof BlockHitResult) {
      BlockPos blockPos = ((BlockHitResult) result).getBlockPos();
      BlockState blockState = performer.getLevel().getBlockState(blockPos);
      if (this.blockPosTarget == null || this.blockStateTarget == null) {
        this.blockPosTarget = blockPos;
        this.blockStateTarget = blockState;
        if (!this.type.getPredicate().test(this.blockStateTarget)) {
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
}
