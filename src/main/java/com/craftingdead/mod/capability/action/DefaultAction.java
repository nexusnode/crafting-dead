package com.craftingdead.mod.capability.action;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public class DefaultAction implements IAction {

  @Override
  public boolean isActive(ClientPlayerEntity playerEntity) {
    return false;
  }

  @Override
  public ITextComponent getText(ClientPlayerEntity playerEntity) {
    return null;
  }

  @Override
  public float getProgress(ClientPlayerEntity playerEntity) {
    return 0;
  }
}
