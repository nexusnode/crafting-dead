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

package com.craftingdead.core.world.item;

import java.util.function.Supplier;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ActionItem extends Item {

  private final Supplier<? extends ItemActionType<?>> itemActionType;

  public ActionItem(Supplier<? extends ItemActionType<?>> itemActionType, Properties properties) {
    super(properties);
    this.itemActionType = itemActionType;
  }

  public ItemActionType<?> getActionType() {
    return this.itemActionType.get();
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    if (!context.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(context.getPlayer());
      if (this.getActionType().createBlockAction(performer, context)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResult.CONSUME;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResult interactLivingEntity(ItemStack itemStack, Player player,
      LivingEntity targetEntity, InteractionHand hand) {
    if (!player.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(player);
      var target = LivingExtension.getOrThrow(targetEntity);
      if (this.getActionType().createEntityAction(performer, target, hand)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResult.CONSUME;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    if (!player.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(player);
      if (this.getActionType().createAction(performer, hand)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResultHolder.consume(player.getItemInHand(hand));
      }
    }

    return InteractionResultHolder.pass(player.getItemInHand(hand));
  }

  @Override
  public int getUseDuration(ItemStack itemStack) {
    return this.getActionType().getTotalDurationTicks();
  }
}
