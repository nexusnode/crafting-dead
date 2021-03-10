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

package com.craftingdead.core.item;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SimpleCapabilityProvider;
import com.craftingdead.core.capability.actionprovider.IActionProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ActionItem extends Item {

  private final IActionProvider actionProvider;

  public ActionItem(Properties properties, IActionProvider actionProvider) {
    super(properties);
    this.actionProvider = actionProvider;
  }

  @Override
  public ActionResultType interactLivingEntity(ItemStack itemStack, PlayerEntity playerEntity,
      LivingEntity targetEntity, Hand hand) {
    if (!playerEntity.getCommandSenderWorld().isClientSide()) {
      this.performAction(playerEntity, targetEntity);
    }
    return ActionResultType.PASS;
  }

  @Override
  public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity,
      Hand hand) {
    if (!playerEntity.getCommandSenderWorld().isClientSide()) {
      this.performAction(playerEntity, null);
    }
    return new ActionResult<>(ActionResultType.PASS, playerEntity.getItemInHand(hand));
  }

  private void performAction(LivingEntity performerEntity, LivingEntity targetEntity) {
    if (this.actionProvider != null) {
      performerEntity.getCapability(ModCapabilities.LIVING)
          .ifPresent(performer -> this.actionProvider
              .getEntityAction(performer,
                  targetEntity == null ? null
                      : targetEntity.getCapability(ModCapabilities.LIVING).orElse(null))
              .ifPresent(action -> performer.performAction(action, false, true)));
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundNBT nbt) {
    return new SimpleCapabilityProvider<>(this.actionProvider,
        () -> ModCapabilities.ACTION_PROVIDER);
  }
}
