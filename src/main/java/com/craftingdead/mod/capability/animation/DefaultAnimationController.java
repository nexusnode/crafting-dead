package com.craftingdead.mod.capability.animation;

import java.util.LinkedList;
import java.util.Queue;

public class DefaultAnimationController implements IAnimationController {

  private final Queue<IAnimation> animations = new LinkedList<>();

  @Override
  public void tick() {
    IAnimation animation = this.animations.peek();
    if (animation.tick()) {
      this.animations.poll();
    }
  }

  @Override
  public void addAnimation(IAnimation animation) {
    this.animations.add(animation);
  }

  @Override
  public void cancelCurrentAnimation() {
    this.animations.poll();
  }

  @Override
  public void clearAnimations() {
    this.animations.clear();
  }

  @Override
  public IAnimation getCurrentAnimation() {
    return this.animations.peek();
  }
}
