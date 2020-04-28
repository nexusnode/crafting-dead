package com.craftingdead.immerse.client.gui.component;

public class CompositeSize implements ISize {

  private final ISize width;
  private final ISize height;

  public CompositeSize(ISize width, ISize height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth(Component<?> component) {
    return this.width.getWidth(component);
  }

  @Override
  public int getHeight(Component<?> component) {
    return this.height.getHeight(component);
  }
}
