package com.craftingdead.immerse.client.gui.view.style.selector;

import com.craftingdead.immerse.client.gui.view.style.StyleNode;

/**
 * Identifies a single state associated with a {@link StyleNode}.
 * 
 * @author Sm0keySa1m0n
 */
public record StyleNodeState(StyleNode node, int state) {

  /**
   * Check if the state represented by this object is present.
   * 
   * @return <code>true</code> if does, <code>false</code> otherwise
   */
  public boolean check() {
    return this.node.getStyleManager().hasState(this.state);
  }
}
