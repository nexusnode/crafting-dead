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

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class ToolItem extends Item {

  public ToolItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean canAttackBlock(BlockState blockState, Level world,
      BlockPos blockPos, Player playerEntity) {
    return !playerEntity.isCreative();
  }

  @Override
  public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
    if (blockState.is(Blocks.COBWEB)) {
      return 15.0F;
    } else {
      Material material = blockState.getMaterial();
      return material != Material.PLANT
          && material != Material.REPLACEABLE_PLANT
          && material != Material.WATER_PLANT
          && !blockState.is(BlockTags.LEAVES)
          && material != Material.VEGETABLE ? 1.0F : 1.5F;
    }
  }

  @Override
  public boolean hurtEnemy(ItemStack itemStack, LivingEntity targetEntity,
      LivingEntity attackerEntity) {
    itemStack.hurtAndBreak(1, attackerEntity,
        (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    return true;
  }

  @Override
  public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos,
      LivingEntity entityLiving) {
    if (state.getDestroySpeed(worldIn, pos) != 0.0F) {
      stack.hurtAndBreak(2, entityLiving,
          (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    }
    return true;
  }

  @Override
  public boolean isCorrectToolForDrops(BlockState blockIn) {
    return blockIn.is(Blocks.COBWEB);
  }
}
