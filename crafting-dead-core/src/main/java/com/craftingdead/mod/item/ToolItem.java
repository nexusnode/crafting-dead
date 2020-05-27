package com.craftingdead.mod.item;

import com.craftingdead.mod.util.Text;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class ToolItem extends Item {

  public static final ITextComponent CAN_OPEN_CANNED_ITEMS_TOOLTIP =
      Text.translate("item_lore.tool_item.open_canned_items").applyTextStyle(TextFormatting.GRAY);

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
