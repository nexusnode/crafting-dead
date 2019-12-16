package com.craftingdead.mod.capability.action;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

public interface IAction {

  boolean isActive(PlayerEntity playerEntity);

  ITextComponent getText(PlayerEntity playerEntity);

  double getPercentComplete(PlayerEntity playerEntity);
}
