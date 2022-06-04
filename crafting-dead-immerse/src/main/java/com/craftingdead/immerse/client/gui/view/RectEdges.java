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

package com.craftingdead.immerse.client.gui.view;

import java.util.Iterator;
import com.google.common.collect.Iterators;

public record RectEdges<T> (T top, T right, T bottom, T left) implements Iterable<T> {

  public T at(BoxSide side) {
    return switch (side) {
      case TOP -> top;
      case RIGHT -> right;
      case BOTTOM -> bottom;
      case LEFT -> left;
    };
  }

  @Override
  public Iterator<T> iterator() {
    return Iterators.forArray(this.top, this.right, this.bottom, this.left);
  }
}
