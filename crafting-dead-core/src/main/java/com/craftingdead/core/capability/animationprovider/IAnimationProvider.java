package com.craftingdead.core.capability.animationprovider;

public interface IAnimationProvider<T extends IAnimationController> {

  T getAnimationController();
}
