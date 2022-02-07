package com.craftingdead.immerse.client.gui.view.style.adapter;

@FunctionalInterface
public interface StyleEncoder<T> {

  String encode(T value, boolean prettyPrint);
}
