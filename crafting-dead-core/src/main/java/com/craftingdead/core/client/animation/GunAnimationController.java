/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.client.animation;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.tuple.Pair;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class GunAnimationController {

  private final Queue<Pair<GunAnimation, Runnable>> animations = new LinkedList<>();

  private final Minecraft minecraft = Minecraft.getInstance();

  private int ticks;

  public void tick(LivingEntity livingEntity, ItemStack itemStack) {
    Pair<GunAnimation, Runnable> animation = this.animations.peek();
    if (animation != null) {
      this.ticks++;
      float progress =
          MathHelper.clamp(this.ticks / (animation.getLeft().getMaxAnimationTick() * 1.5F), 0.0F,
              1.0F);
      animation.getLeft().onUpdate(this.minecraft, livingEntity, itemStack, progress);
      if (progress >= 1.0F) {
        if (animation.getRight() != null) {
          animation.getRight().run();
        }
        this.removeCurrentAnimation();
      }
    }
  }

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
