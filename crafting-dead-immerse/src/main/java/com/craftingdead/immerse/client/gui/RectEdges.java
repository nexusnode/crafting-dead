package com.craftingdead.immerse.client.gui;

public record RectEdges<T> (T top, T right, T bottom, T left) {

  public T at(BoxSide side) {
    return switch (side) {
      case TOP -> top;
      case RIGHT -> right;
      case BOTTOM -> bottom;
      case LEFT -> left;
    };
  }
}
