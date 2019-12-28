package com.craftingdead.mod.capability.action;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class DefaultAction implements IAction {

  @Override
  public boolean isActive(PlayerEntity playerEntity, ItemStack itemStack) {
    return playerEntity.isHandActive();
  }

  @Override
  public ITextComponent getText(PlayerEntity playerEntity, ItemStack itemStack) {
    return itemStack.getDisplayName();
  }

  @Override
  public float getPercentComplete(PlayerEntity playerEntity, ItemStack itemStack) {
    return (float) playerEntity.getItemInUseMaxCount()
        / (float) playerEntity.getActiveItemStack().getUseDuration();
  }
}
