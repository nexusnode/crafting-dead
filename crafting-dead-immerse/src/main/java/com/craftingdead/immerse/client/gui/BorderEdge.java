package com.craftingdead.immerse.client.gui;

import com.craftingdead.immerse.client.gui.view.Color;

public record BorderEdge(float edgeWidth, Color edgeColor) {

  public boolean shouldRender() {
    return this.edgeWidth > 0.0F && !this.edgeColor.transparent();
  }
}
