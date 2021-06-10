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

package com.craftingdead.core.client.animation.reload;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class GunAnimationReloadG18 extends GunAnimationReload {

  protected float rotation1 = 0F;
  protected float lastRotation1 = 0F;
  protected float maxRotation1 = 55;

  protected float trans1 = -1F;
  protected float lastTrans1 = 0F;
  protected float maxTrans1 = 0.3F;

  protected float armMovement = 0.4F;

  protected boolean up = true;

  /**
   * When set to false, the clip will be loaded into the gun
   */
  public boolean ejectingClip = false;

  private int ticks;

  public GunAnimationReloadG18() {
    this.ejectingClip = true;
    trans1 = 0F;
  }

  public GunAnimationReloadG18(boolean par1) {
    this.ejectingClip = par1;
    trans1 = this.ejectingClip ? 0F : -1F;
  }

  public void setEjectingClip(boolean par1) {
    this.ejectingClip = par1;
    trans1 = this.ejectingClip ? 0F : -1F;
  }

  @Override
  public void onUpdate(Minecraft par1, LivingEntity par2, ItemStack par3, float progress) {
    ticks++;
    if (progress * this.getMaxAnimationTick() >= 20) {
      up = false;
    }

    lastRotation1 = rotation1;
    lastTrans1 = trans1;

    float roation1Speed = 10F;
    float transSpeed = 0.1F;

    if (this.ejectingClip) {
      trans1 -= transSpeed;
    } else {
      trans1 += transSpeed;

      if (trans1 > 0) {
        trans1 = 0;
      }
    }

    if (up) {
      rotation1 += roation1Speed;
    } else {
      rotation1 -= roation1Speed;
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
    float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);

    matrixStack.mulPose(new Vector3f(-0.5F, 0.5F, -0.5F).rotationDegrees(-progress));
    matrixStack.mulPose(Vector3f.ZN.rotationDegrees(progress));
  }

  public void doRenderAmmo(ItemStack par1, float par2, MatrixStack matrixStack) {
    if (this.ejectingClip) {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(0, -transprogress, 0);
    } else {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(transprogress, -transprogress, 0);
    }
  }

  @Override
  public void onAnimationStopped(ItemStack par1) {}

  @Override
  public float getMaxAnimationTick() {
    return 50;
  }

  @Override
  public void doRenderHand(ItemStack par1, float par2, boolean par3, MatrixStack matrixStack) {

    if (par3) {
      float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);
      matrixStack.mulPose(new Vector3f(1.0F, 0.0F, 1.0F).rotationDegrees(progress * 0.2F));
    } else {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(-transprogress * 1F, transprogress * 0.5F, transprogress);

      float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(progress * 0.2F));
    }

    if (!this.ejectingClip && !par3 && ticks >= (getMaxAnimationTick() / 2)) {

      float ticks = this.ticks - (getMaxAnimationTick() / 2);

      float x = this.armMovement - 0.40F;
      float y = this.armMovement - 0.05F;

      if (ticks < 12) {
        if (ticks >= 5) {
          this.armMovement += 0.015;
        } else {
          this.armMovement -= 0.015;
        }
      }

      matrixStack.translate(x, y, 0.1F);
    }
  }
}
