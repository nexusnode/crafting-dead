package com.craftingdead.immerse.client.gui.component;

public class CentredAbsoluteLocation extends AbsoluteLocation {

  public CentredAbsoluteLocation(int coordinate) {
    super(coordinate);
  }

  public CentredAbsoluteLocation(int x, int y) {
    super(x, y);
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
