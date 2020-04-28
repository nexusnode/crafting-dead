package com.craftingdead.immerse.client.gui.component;

import net.minecraft.util.math.MathHelper;

public class PercentSize implements ISize {

  private final float widthPercent;
  private final float heightPercent;

  public PercentSize(float percent) {
    this(percent, percent);
  }

  public PercentSize(float widthPercent, float heightPercent) {
    this.widthPercent = widthPercent;
    this.heightPercent = heightPercent;
  }

  @Override
  public int getWidth(Component<?> component) {
    return MathHelper.ceil(component.getParentProperty().get().getWidth() * this.widthPercent) - component.getX();
  }

  @Override
  public int getHeight(Component<?> component) {
    return MathHelper.ceil(component.getParentProperty().get().getHeight() * this.heightPercent) - component.getY();
  }
}
