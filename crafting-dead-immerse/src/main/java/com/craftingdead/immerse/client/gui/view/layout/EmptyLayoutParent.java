package com.craftingdead.immerse.client.gui.view.layout;

public class EmptyLayoutParent implements LayoutParent {

  @Override
  public Layout addChild(int index) {
    return Layout.NILL;
  }

  @Override
  public void removeChild(Layout layout) {}

  @Override
  public void layout(float width, float height) {}

  @Override
  public void close() {}
}
