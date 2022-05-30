package com.craftingdead.immerse.client.gui.view.style.selector;

import com.craftingdead.immerse.client.gui.view.style.StyleNode;

public record StyleNodeState(StyleNode node, int state) {

  public int getCombinedState() {
    return this.state == 0 ? 0 : this.node.hashCode() ^ this.state;
  }
}
