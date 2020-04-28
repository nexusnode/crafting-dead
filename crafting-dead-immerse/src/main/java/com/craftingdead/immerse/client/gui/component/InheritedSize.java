package com.craftingdead.immerse.client.gui.component;

public class InheritedSize implements ISize {

  @Override
  public int getWidth(Component<?> component) {
    return component.getParentProperty().getOptional().map(ParentComponent::getWidth).orElse(0);
  }

  @Override
  public int getHeight(Component<?> component) {
    return component.getParentProperty().getOptional().map(ParentComponent::getHeight).orElse(0);
  }
}
