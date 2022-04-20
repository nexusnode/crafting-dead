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

package com.craftingdead.immerse.client.gui.view.style.shorthand;

@FunctionalInterface
public interface ShorthandArgMapper {

  ShorthandArgMapper TWO = (index, count) -> switch (count) {
    case 1 -> new int[] {0, 1};
    default -> new int[0];
  };

  ShorthandArgMapper BOX_MAPPER = (index, count) -> switch (count) {
    case 1 -> new int[] {0, 1, 2, 3}; // top, right, bottom, left
    case 2 -> {
      if (index == 0)
        yield new int[] {0, 2}; // top, bottom
      yield new int[] {1, 3}; // right, left
    }
    case 3 -> {
      if (index == 0)
        yield new int[] {0}; // top
      if (index == 2)
        yield new int[] {2}; // bottom
      yield new int[] {1, 3}; // right, left
    }
    case 4 -> new int[] {index};
    default -> new int[0];
  };

  /**
   * Map a shorthand syntax to child properties indexes
   *
   * @param index of the current argument (0 ... n)
   * @param count numbers of specified arguments (1 ... n)
   * @return an array containing the indexes of the child properties to which apply the current
   *         argument
   */
  int[] map(int index, int count);
}
