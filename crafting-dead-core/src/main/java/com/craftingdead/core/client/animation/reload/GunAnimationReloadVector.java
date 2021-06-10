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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class GunAnimationReloadVector extends GunAnimationReload {

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
  }

  @Override
  public void doRender(ItemStack par1, float par2, MatrixStack matrixStack) {
    float progress = lastRotation1 + (rotation1 - lastRotation1) * par2;
    matrixStack.mulPose(new Vector3f(2.0F, 0.0F, -1.0F).rotationDegrees(progress));
  }
}

