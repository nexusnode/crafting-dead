package com.craftingdead.mod.capability.action;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.text.ITextComponent;

public class DefaultAction implements IAction {

  @Override
  public boolean isActive(PlayerEntity playerEntity) {
    return playerEntity.isHandActive()
        && !(playerEntity.getActiveItemStack().getItem() instanceof ShootableItem)
        && playerEntity.getActiveItemStack().getUseDuration() != 72000;
  }

  @Override
  public ITextComponent getText(PlayerEntity playerEntity) {
    return playerEntity.getActiveItemStack().getDisplayName();
  }

  @Override
  public double getPercentComplete(PlayerEntity playerEntity) {
    return (double) playerEntity.getItemInUseMaxCount()
        / (double) playerEntity.getActiveItemStack().getUseDuration();
  }
}
