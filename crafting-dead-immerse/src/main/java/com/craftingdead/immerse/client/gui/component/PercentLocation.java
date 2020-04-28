package com.craftingdead.immerse.client.gui.component;

import net.minecraft.util.math.MathHelper;

public class PercentLocation extends InheritedLocation {

  private final float widthPercent;
  private final float heightPercent;

  public PercentLocation(float percent) {
    this(percent, percent);
  }

  public PercentLocation(float widthPercent, float heightPercent) {
    this.widthPercent = widthPercent;
    this.heightPercent = heightPercent;
  }

  @Override
  public int getX(Component<?> component) {
    return super.getX(component)
        + MathHelper.ceil(this.widthPercent * component.getParentProperty().get().getWidth());
  }

  @Override
  public int getY(Component<?> component) {
    return super.getY(component)
        + MathHelper.ceil(this.heightPercent * component.getParentProperty().get().getHeight());
  }
}
