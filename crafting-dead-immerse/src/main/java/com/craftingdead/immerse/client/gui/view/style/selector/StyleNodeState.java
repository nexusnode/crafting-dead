/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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
