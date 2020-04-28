package com.craftingdead.immerse.client.gui.component;

public class AbsoluteLocation extends InheritedLocation {

  private final Integer x;
  private final Integer y;

  public AbsoluteLocation(int coordinate) {
    this(coordinate, coordinate);
  }

  public AbsoluteLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int getX(Component<?> component) {
    return super.getX(component) + this.x;
  }

  @Override
  public int getY(Component<?> component) {
    return super.getY(component) + this.y;
  }
}
