package com.craftingdead.mod.capability.action;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public interface IAction {

  boolean isActive(PlayerEntity playerEntity, ItemStack itemStack);

  ITextComponent getText(PlayerEntity playerEntity, ItemStack itemStack);

  float getPercentComplete(PlayerEntity playerEntity, ItemStack itemStack);
}
