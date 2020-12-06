/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ToolItem extends Item {

  public static final ITextComponent CAN_OPEN_CANNED_ITEMS_TOOLTIP =
      Text.translate("item_lore.tool_item.open_canned_items").mergeStyle(TextFormatting.GRAY);

  public ToolItem(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack getContainerItem(ItemStack itemStack) {
    ItemStack damagedStack = itemStack.copy();
    damagedStack.setDamage(damagedStack.getDamage() + 1);
    return damagedStack.getDamage() >= damagedStack.getMaxDamage() ? ItemStack.EMPTY : damagedStack;
  }

  @Override
  public boolean hasContainerItem(ItemStack stack) {
    return true;
  }
}
