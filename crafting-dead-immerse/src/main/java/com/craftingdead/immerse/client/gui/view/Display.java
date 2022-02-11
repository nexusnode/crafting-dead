package com.craftingdead.immerse.client.gui.view;

import java.util.function.Supplier;
import com.craftingdead.immerse.client.gui.view.layout.LayoutParent;
import com.craftingdead.immerse.client.gui.view.layout.yoga.YogaLayoutParent;

public enum Display {

  FLEX(YogaLayoutParent::new);

  private final Supplier<LayoutParent> layoutParentFactory;

  private Display(Supplier<LayoutParent> layoutParentFactory) {
    this.layoutParentFactory = layoutParentFactory;
  }

  public LayoutParent createLayoutParent() {
    return this.layoutParentFactory.get();
  }
}
