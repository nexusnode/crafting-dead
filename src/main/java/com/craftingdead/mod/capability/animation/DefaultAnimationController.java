package com.craftingdead.mod.capability.animation;

import java.util.LinkedList;
import java.util.Queue;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class DefaultAnimationController implements IAnimationController {

  private final Queue<IAnimation> animations = new LinkedList<>();

  private long startTime = 0L;

  @Override
  public void addAnimation(IAnimation animation) {
    this.animations.add(animation);
  }

  @Override
  public void cancelCurrentAnimation() {
    this.animations.poll();
    this.startTime = 0L;
  }

  @Override
  public void clearAnimations() {
    this.animations.clear();
  }

  @Override
  public void apply(MatrixStack matrixStack) {
    IAnimation animation = this.animations.peek();
    if (animation != null) {
      if (this.startTime == 0L) {
        this.startTime = Util.milliTime();
      }
      float progress =
          MathHelper.clamp((Util.milliTime() - this.startTime) / animation.getLength(), 0.0F, 1.0F);
      animation.apply(matrixStack, progress);
      if (progress >= 1.0D) {
        this.cancelCurrentAnimation();
      }
    }
  }
}
