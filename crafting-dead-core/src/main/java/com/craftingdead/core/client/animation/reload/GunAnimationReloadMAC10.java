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

public class GunAnimationReloadMAC10 extends GunAnimationReload {

  protected float armMovement = 0.3F;

  private int ticks;

  @Override
  public void onUpdate(Minecraft par1, LivingEntity par2, ItemStack par3, float progress) {
    super.onUpdate(par1, par2, par3, progress);
    this.ticks++;
  }

  @Override
  public void doRenderHand(ItemStack par1, float par2, boolean par3, MatrixStack matrixStack) {

    if (this.ticks < (getMaxAnimationTick() / 2)) {
      if (par3) {
        float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);
        matrixStack.mulPose(new Vector3f(1.0F, 0.0F, 1.0F).rotationDegrees(progress * 0.2F));
      } else {
        float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
        matrixStack.translate(-transprogress * 1F, transprogress * 0.5F, transprogress);

        float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(progress * 0.2F));
      }
    }

    if (!this.ejectingClip && !par3 && this.ticks >= 30) {

      this.armMovement -= 0.010;

      matrixStack.translate((this.armMovement - 0.55F), this.armMovement - 0.05F, 0.1F);
    }
  }

  @Override
  public void doRender(ItemStack par1, float par2, MatrixStack matrixStack) {
    float progress = lastRotation1 + (rotation1 - lastRotation1) * par2;
    matrixStack.mulPose(new Vector3f(2.2F, -0.5F, -1.0F).rotationDegrees(progress));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-progress * 0.25F));
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
  public float getMaxAnimationTick() {
    return 40;
  }
}

