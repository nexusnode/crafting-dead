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

package com.craftingdead.core.client.animation.inspect;

import com.craftingdead.core.client.animation.GunAnimation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public abstract class GunAnimationInspect extends GunAnimation {

  protected float rotation1 = 0F;
  protected float lastRotation1 = 0F;
  protected float maxRotation1 = 55;

  protected float trans1 = 0F;
  protected float lastTrans1 = 0F;
  protected float maxTrans1 = 0.6F;

  protected boolean up = true;

  @Override
  public void onUpdate(Minecraft par1, LivingEntity par2, ItemStack par3, float progress) {

    if (progress > 0.8F) {
      this.up = false;
    }

    lastRotation1 = rotation1;
    lastTrans1 = trans1;

    float roation1Speed = 15F;
    float transSpeed = 0.15F;

    if (up) {

      trans1 += transSpeed;

      if (trans1 > maxTrans1) {
        trans1 = maxTrans1;
      }

      rotation1 += roation1Speed;
    } else {
      rotation1 -= roation1Speed;
      trans1 -= transSpeed;

      if (trans1 < 0) {
        trans1 = 0;
      }
    }

    if (rotation1 > maxRotation1) {
      rotation1 = maxRotation1;
    }

    if (rotation1 < 0) {
      rotation1 = 0;
    }
  }

  @Override
  public void doRender(ItemStack par1, float par2, MatrixStack matrixStack) {
    float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
    matrixStack.translate(transprogress, 0, transprogress);

    float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);

    matrixStack.mulPose(Vector3f.YP.rotationDegrees(-progress));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(progress / 3));
  }

  @Override
  public void onAnimationStopped(ItemStack par1) {}

  @Override
  public float getMaxAnimationTick() {
    return 100;
  }
  
  @Override
  protected boolean isAcceptedTransformType(ItemCameraTransforms.TransformType transformType) {
    return (transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND
        || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) ? false
            : super.isAcceptedTransformType(transformType);
  }
}
