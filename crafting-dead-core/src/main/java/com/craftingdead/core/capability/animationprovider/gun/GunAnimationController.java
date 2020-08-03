package com.craftingdead.core.capability.animationprovider.gun;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.animationprovider.IAnimationController;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class GunAnimationController implements IAnimationController {

  private final Queue<Pair<GunAnimation, Runnable>> animations = new LinkedList<>();

  private final Minecraft minecraft = Minecraft.getInstance();

  private long animationStartTime = 0L;

  @Override
  public void tick(LivingEntity livingEntity, ItemStack itemStack) {
    Pair<GunAnimation, Runnable> animation = this.animations.peek();
    if (animation != null) {
      if (this.animationStartTime == 0L) {
        this.animationStartTime = Util.milliTime();
      }
      float progress =
          MathHelper.clamp(
              (Util.milliTime() - this.animationStartTime) / animation.getLeft().getLength(),
              0.0F, 1.0F);
      animation.getLeft().onUpdate(this.minecraft, livingEntity, itemStack, progress);
      if (progress >= 1.0F) {
        if (animation.getRight() != null) {
          animation.getRight().run();
        }
        this.removeCurrentAnimation();
      }
    }
  }

  @Override
  public void applyTransforms(LivingEntity livingEntity, ItemStack itemStack, String part,
      MatrixStack matrixStack) {
    Pair<GunAnimation, Runnable> animationPair = this.animations.peek();
    if (animationPair != null) {
      animationPair.getLeft().applyTransforms(livingEntity, itemStack, part, matrixStack,
          this.minecraft.getRenderPartialTicks());
    }
  }

  public void addAnimation(GunAnimation animation, Runnable callback) {
    this.animations.add(Pair.of(animation, callback));
  }

  public void removeCurrentAnimation() {
    this.animations.poll();
    this.animationStartTime = 0L;
  }

  public void clearAnimations() {
    this.animations.clear();
  }
}
