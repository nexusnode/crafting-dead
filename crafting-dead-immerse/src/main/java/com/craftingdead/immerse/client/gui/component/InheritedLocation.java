package com.craftingdead.immerse.client.gui.component;

public class InheritedLocation implements ILocation {

  @Override
  public int getX(Component<?> component) {
    return component.getParentProperty().getOptional().map(ParentComponent::getX).orElse(0);
  }

  @Override
  public int getY(Component<?> component) {
    return component.getParentProperty().getOptional().map(ParentComponent::getY).orElse(0);
  }
}
