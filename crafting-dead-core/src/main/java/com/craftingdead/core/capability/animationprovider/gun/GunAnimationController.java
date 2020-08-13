package com.craftingdead.core.capability.animationprovider.gun;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.animationprovider.IAnimationController;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class GunAnimationController implements IAnimationController {

  private final Queue<Pair<GunAnimation, Runnable>> animations = new LinkedList<>();

  private final Minecraft minecraft = Minecraft.getInstance();

  private int ticks;

  @Override
  public void tick(LivingEntity livingEntity, ItemStack itemStack) {
    Pair<GunAnimation, Runnable> animation = this.animations.peek();
    if (animation != null) {
      this.ticks++;
      float progress =
          MathHelper.clamp(this.ticks / animation.getLeft().getMaxAnimationTick(), 0.0F, 1.0F);
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
      ItemCameraTransforms.TransformType transformType,
      MatrixStack matrixStack, float partialTicks) {
    Pair<GunAnimation, Runnable> animationPair = this.animations.peek();
    if (animationPair != null && animationPair.getLeft().isAcceptedTransformType(transformType)) {
      animationPair.getLeft().applyTransforms(livingEntity, itemStack, part, matrixStack,
          partialTicks);
    }
  }

  public void addAnimation(GunAnimation animation, Runnable callback) {
    this.animations.add(Pair.of(animation, callback));
  }

  public void removeCurrentAnimation() {
    this.animations.poll();
    this.ticks = 0;
  }

  public void clearAnimations() {
    this.animations.clear();
  }
}
