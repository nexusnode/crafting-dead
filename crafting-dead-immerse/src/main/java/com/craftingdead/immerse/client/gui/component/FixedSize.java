package com.craftingdead.immerse.client.gui.component;

public class FixedSize implements ISize {

  private final int width;
  private final int height;
  
  public FixedSize(int size) {
    this.width = size;
    this.height = size;
  }

  public FixedSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth(Component<?> component) {
    return this.width;
  }

  @Override
  public int getHeight(Component<?> component) {
    return this.height;
  }
}
