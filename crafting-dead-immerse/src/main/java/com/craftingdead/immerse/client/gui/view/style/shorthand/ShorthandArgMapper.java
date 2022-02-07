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
