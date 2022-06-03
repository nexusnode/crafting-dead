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

package com.craftingdead.immerse.client.gui.view.style;

import java.util.Set;
import com.craftingdead.immerse.client.gui.view.property.Transition;
import com.craftingdead.immerse.client.gui.view.style.selector.StyleNodeState;

/**
 * An abstract style property which may dispatch to multiple properties or just a single property.
 * 
 * @author Sm0keySa1m0n
 *
 * @param <T> - proeprty type
 */
public interface PropertyDispatcher<T> {

  void setTransition(Transition transition);

  void defineState(String value, int specificity, Set<StyleNodeState> nodeStates);

  void refreshState();

  void reset();

  default int validate(String style) {
    return style.length();
  }

  String getName();
}
