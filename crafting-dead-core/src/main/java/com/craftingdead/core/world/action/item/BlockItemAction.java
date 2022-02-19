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

import com.craftingdead.core.util.RayTraceUtil;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockItemAction extends ItemAction {

  private final BlockItemActionType type;
  private final LivingExtension<?, ?> performer;
  private final UseOnContext context;

  private BlockState blockState;

  protected BlockItemAction(BlockItemActionType type,
      LivingExtension<?, ?> performer, UseOnContext context) {
    super(context.getHand());
    this.type = type;
    this.performer = performer;
    this.context = context;
  }

  protected BlockItemAction(InteractionHand hand, BlockItemActionType type,
      LivingExtension<?, ?> performer, BlockHitResult hitResult) {
    super(hand);
    this.type = type;
    this.performer = performer;
    this.context = new UseOnContext(performer.getLevel(),
        performer.getEntity() instanceof Player player ? player : null, hand,
        performer.getEntity().getItemInHand(hand), hitResult);
  }

  public UseOnContext getContext() {
    return this.context;
  }

  public BlockState getBlockState() {
    return this.blockState;
  }

  @Override
  public boolean start(boolean simulate) {
    this.blockState = this.performer.getLevel().getBlockState(this.context.getClickedPos());
    return super.start(simulate) && this.type.getPredicate().test(this.blockState);
  }

  @Override
  public boolean tick() {
    var result = RayTraceUtil.pick(this.performer.getEntity()).orElse(null);
    if (result == null
        || !result.getBlockPos().equals(this.context.getClickedPos())
        || !this.blockState.equals(this.performer.getLevel().getBlockState(result.getBlockPos()))) {
      this.performer.cancelAction(true);
      return false;
    }
    return super.tick();
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }

  @Override
  public ItemActionType<?> getType() {
    return this.type;
  }
}
