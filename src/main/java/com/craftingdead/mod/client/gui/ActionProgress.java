package com.craftingdead.mod.client.gui;

import net.minecraft.util.text.ITextComponent;

public class ActionProgress {

  private ITextComponent text;
  private float percentComplete;

  public ActionProgress(ITextComponent text, float percentComplete) {
    this.text = text;
    this.percentComplete = percentComplete;
  }

  public ITextComponent getText() {
    return text;
  }

  public void setText(ITextComponent text) {
    this.text = text;
  }

  public float getPercentComplete() {
    return percentComplete;
  }

  public void setPercentComplete(float percentComplete) {
    this.percentComplete = percentComplete;
  }
}
