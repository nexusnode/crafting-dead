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

package com.craftingdead.core.client.animation.fire;

import com.craftingdead.core.client.animation.GunAnimation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class RifleShootAnimation extends GunAnimation {
  private float rotation1 = 0F;
  private float lastRotation1 = 0F;
  private float maxRotation1 = 2;

  private float trans1 = 0F;
  private float lastTrans1 = 0F;
  private float maxTrans1 = 0.3F;

  private boolean up = true;

  @Override
  public void onUpdate(Minecraft par1, LivingEntity par2, ItemStack par3, float progress) {
    lastRotation1 = rotation1;
    lastTrans1 = trans1;

    float roation1Speed = 60F;
    float transSpeed = 0.25F;

    if (progress >= 0.5F) {
      this.up = false;
    }

    if (up) {
      rotation1 += roation1Speed;
      trans1 += transSpeed;
    } else {
      rotation1 -= roation1Speed;
      trans1 -= transSpeed;
    }

    if (trans1 > maxTrans1) {
      trans1 = maxTrans1;
    }

    if (trans1 < 0) {
      trans1 = 0;
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
    matrixStack.translate(-transprogress, 0, 0);

    float rotprogress = lastRotation1 + (rotation1 - lastRotation1) * par2;
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-rotprogress));
  }

  @Override
  public void doRenderHand(ItemStack par1, float par2, boolean par3, MatrixStack matrixStack) {
    if (par3) {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(-transprogress, 0, 0);

      float rotprogress = lastRotation1 + (rotation1 - lastRotation1) * par2;
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-rotprogress));
    } else {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(-transprogress, -transprogress * 0.5F, 0);

      float rotprogress = lastRotation1 + (rotation1 - lastRotation1) * par2;
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-rotprogress));
    }
  }

  public void doRenderAmmo(ItemStack par1, float par2) {}

  @Override
  public void onAnimationStopped(ItemStack par1) {}

  @Override
  public float getMaxAnimationTick() {
    return 4;
  }
}
