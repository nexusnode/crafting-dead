package com.craftingdead.immerse.client.gui.component;

public class CentredPercentLocation extends PercentLocation {

  public CentredPercentLocation(float percent) {
    super(percent);
  }

  public CentredPercentLocation(float widthPercent, float heightPercent) {
    super(widthPercent, heightPercent);
  }

  @Override
  public int getX(Component<?> component) {
    return super.getX(component) - component.getWidth() / 2;
  }

  @Override
  public int getY(Component<?> component) {
    return super.getY(component) - component.getHeight() / 2;
  }
}
