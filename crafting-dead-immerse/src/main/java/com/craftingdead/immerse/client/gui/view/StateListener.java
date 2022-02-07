package com.craftingdead.immerse.client.gui.view;

public interface StateListener {

  boolean transition(int state, boolean animate);
}
