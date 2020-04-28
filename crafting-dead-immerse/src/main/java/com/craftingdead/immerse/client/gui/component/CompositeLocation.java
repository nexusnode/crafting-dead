package com.craftingdead.immerse.client.gui.component;

import java.util.function.IntBinaryOperator;

public class CompositeLocation implements ILocation {

  private final ILocation a;
  private final ILocation b;
  private final IntBinaryOperator operator;

  public CompositeLocation(ILocation a, ILocation b, IntBinaryOperator operator) {
    this.a = a;
    this.b = b;
    this.operator = operator;
  }

  @Override
  public int getX(Component<?> component) {
    return this.operator.applyAsInt(this.a.getX(component), this.b.getX(component));
  }

  @Override
  public int getY(Component<?> component) {
    return this.operator.applyAsInt(this.a.getY(component), this.b.getY(component));
  }
}
