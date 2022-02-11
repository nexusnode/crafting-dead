package com.craftingdead.immerse.client.gui.view.style.adapter;

@FunctionalInterface
public interface StyleDecoder<T> {

  /**
   * Decode a css string and return the value.
   *
   * @param style css string
   * @return the decoded value
   */
  T decode(String style);
}
