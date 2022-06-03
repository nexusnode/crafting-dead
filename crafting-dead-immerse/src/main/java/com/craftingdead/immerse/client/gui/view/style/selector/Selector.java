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

import java.util.Optional;
import java.util.Set;
import com.craftingdead.immerse.client.gui.view.style.StyleNode;

/**
 * Represents a CSS selector.
 * 
 * @see <a href="https://drafts.csswg.org/selectors/">https://drafts.csswg.org/selectors/</a>
 * 
 * @author Sm0keySa1m0n
 */
public interface Selector {

  /**
   * Attempt to match the specified {@link StyleNode} against this selector.
   * 
   * @param node - the node to match
   * @return {@link Optional#empty()} if there is no match, otherwise a set of dependent states.
   */
  Optional<Set<StyleNodeState>> match(StyleNode node);

  /**
   * The specificity of this selector.
   * 
   * @return the specificity
   */
  int getSpecificity();
}
