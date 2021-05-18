package com.craftingdead.immerse.client.gui.view.layout;

public interface LayoutParent<L extends Layout> {

  void addChild(L layout, int index);

  void removeChild(L layout);

  void layout(float width, float height);
}
