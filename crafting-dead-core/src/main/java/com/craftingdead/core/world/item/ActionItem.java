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

import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.action.Action;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ActionItem extends Item {

  @Nullable
  private final BiFunction<LivingExtension<?, ?>, BlockPos, Action> blockActionFactory;
  @Nullable
  private final BiFunction<LivingExtension<?, ?>, LivingExtension<?, ?>, Action> entityActionFactory;

  public ActionItem(Properties properties) {
    super(properties);
    this.blockActionFactory = properties.blockActionFactory;
    this.entityActionFactory = properties.entityActionFactory;
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
  public ActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
    if (!playerEntity.level.isClientSide()) {
      this.performAction(playerEntity, null);
    }
    return new ActionResult<>(ActionResultType.PASS, playerEntity.getItemInHand(hand));
  }

  public void performAction(LivingEntity performerEntity, LivingEntity targetEntity) {
    if (this.entityActionFactory != null) {
      performerEntity.getCapability(Capabilities.LIVING)
          .ifPresent(performer -> performer.performAction(
              this.entityActionFactory.apply(performer, targetEntity == null
                  ? null
                  : targetEntity.getCapability(Capabilities.LIVING).orElse(null)),
              false, true));
    }
  }

  public static class Properties extends Item.Properties {

    @Nullable
    private BiFunction<LivingExtension<?, ?>, BlockPos, Action> blockActionFactory;
    @Nullable
    private BiFunction<LivingExtension<?, ?>, LivingExtension<?, ?>, Action> entityActionFactory;

    public Properties setAction(Supplier<? extends ActionType> actionType) {
      // Can't use method reference because don't want to resolve the supplier too early.
      this.entityActionFactory =
          (performer, target) -> actionType.get().createAction(performer, target);
      return this;
    }

    public Properties setBlockFactory(
        BiFunction<LivingExtension<?, ?>, BlockPos, Action> blockFactory) {
      this.blockActionFactory = blockFactory;
      return this;
    }

    public Properties setEntityFactory(
        BiFunction<LivingExtension<?, ?>, LivingExtension<?, ?>, Action> entityFactory) {
      this.entityActionFactory = entityFactory;
      return this;
    }
  }
}
