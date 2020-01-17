package com.craftingdead.mod.capability.animation;

public interface IAnimationController {

  void tick();

  void addAnimation(IAnimation animation);

  void cancelCurrentAnimation();

  void clearAnimations();

  IAnimation getCurrentAnimation();
}
