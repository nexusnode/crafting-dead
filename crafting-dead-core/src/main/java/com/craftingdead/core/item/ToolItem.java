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

import com.craftingdead.core.util.Text;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ToolItem extends Item {

  public static final ITextComponent CAN_OPEN_CANNED_ITEMS_TOOLTIP =
      Text.translate("item_lore.tool_item.open_canned_items").withStyle(TextFormatting.GRAY);

  public ToolItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean canAttackBlock(BlockState blockState, World world,
      BlockPos blockPos, PlayerEntity playerEntity) {
    return !playerEntity.isCreative();
  }

  @Override
  public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
    if (blockState.is(Blocks.COBWEB)) {
      return 15.0F;
    } else {
      Material material = blockState.getMaterial();
      return material != Material.PLANT && material != Material.REPLACEABLE_PLANT
          && material != Material.CORAL && !blockState.is(BlockTags.LEAVES)
          && material != Material.VEGETABLE ? 1.0F : 1.5F;
    }
  }

  @Override
  public boolean hurtEnemy(ItemStack itemStack, LivingEntity targetEntity,
      LivingEntity attackerEntity) {
    itemStack.hurtAndBreak(1, attackerEntity,
        (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
    return true;
  }

  @Override
  public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
      LivingEntity entityLiving) {
    if (state.getDestroySpeed(worldIn, pos) != 0.0F) {
      stack.hurtAndBreak(2, entityLiving,
          (entity) -> entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
    }
    return true;
  }

  @Override
  public boolean isCorrectToolForDrops(BlockState blockIn) {
    return blockIn.is(Blocks.COBWEB);
  }
}
