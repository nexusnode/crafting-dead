package com.craftingdead.immerse.client.gui.component;

public interface ILocation {

  int getX(Component<?> component);

  int getY(Component<?> component);

  default ILocation add(ILocation other) {
    return new CompositeLocation(this, other, Integer::sum);
  }

  default ILocation substract(ILocation other) {
    return new CompositeLocation(this, other, (a, b) -> a - b);
  }
}
