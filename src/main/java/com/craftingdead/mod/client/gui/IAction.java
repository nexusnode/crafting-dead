package com.craftingdead.mod.client.gui;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.text.ITextComponent;

public interface IAction {

  boolean isActive(ClientPlayerEntity playerEntity);

  ITextComponent getText(ClientPlayerEntity playerEntity);

  float getProgress(ClientPlayerEntity playerEntity);

  public static enum DefaultAction implements IAction {
    NONE {
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
    };
  }
}
