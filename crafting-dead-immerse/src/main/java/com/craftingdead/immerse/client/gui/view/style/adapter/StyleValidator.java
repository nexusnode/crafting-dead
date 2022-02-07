package com.craftingdead.immerse.client.gui.view.style.adapter;

@FunctionalInterface
public interface StyleValidator<T> {

  /**
   * Validate a css string and return the consumed length. return 0 for an invalid css String.
   *
   * @param style css string
   * @return the consumed chars count
   */
  int validate(String style);
}
