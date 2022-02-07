package com.craftingdead.immerse.client.gui.view.style;

import java.util.List;

public interface StyleParent {

  List<StyleHolder> getChildStyles();

  int getChildCount();
}
