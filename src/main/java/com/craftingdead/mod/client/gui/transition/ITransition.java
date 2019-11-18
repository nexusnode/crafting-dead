package com.craftingdead.mod.client.gui.transition;

import com.craftingdead.mod.client.gui.transition.TransitionManager.TransitionType;

public interface ITransition {

  void transform(double width, double height, float progress);

  int getTransitionTime();

  TransitionType getTransitionType();
}
