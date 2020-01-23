package com.craftingdead.mod.client.gui;

import net.minecraft.util.text.ITextComponent;

public interface IAction {

  boolean isActive();

  ITextComponent getText();

  float getProgress();

  class Empty implements IAction {

    @Override
    public boolean isActive() {
      return false;
    }

    @Override
    public ITextComponent getText() {
      return null;
    }

    @Override
    public float getProgress() {
      return 0;
    }
  }
}
