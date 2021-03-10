package com.craftingdead.immerse.client.gui.component;

public class FocusableComponent<SELF extends Component<SELF>> extends Component<SELF> {

  protected boolean focused;

  protected void focusChanged(boolean focused) {}

  @Override
  public boolean changeFocus(boolean forward) {
    this.focusChanged(this.focused = !this.focused);
    return this.focused;
  }
}
