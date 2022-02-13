package com.craftingdead.immerse.client.gui.view.state;

public interface StateListener {

  boolean transition(int state, boolean animate);
}
