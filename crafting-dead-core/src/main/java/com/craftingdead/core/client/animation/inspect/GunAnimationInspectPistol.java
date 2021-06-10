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

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class GunAnimationInspectPistol extends GunAnimationInspect {

  @Override
  public void doRenderHand(ItemStack par1, float partialTicks, boolean rightHand,
      MatrixStack matrixStack) {
    if (rightHand) {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * partialTicks;
      matrixStack.translate(transprogress * 0.2F, transprogress * 0.2F, -transprogress * 0.6F);

      float progress = (lastRotation1 + (rotation1 - lastRotation1) * partialTicks);
      matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-progress * 0.1F));

    } else {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * partialTicks;
      matrixStack.translate(-transprogress, 0, -transprogress);

      float progress = (lastRotation1 + (rotation1 - lastRotation1) * partialTicks);
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(progress));
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(-progress / 2));
    }
  }
}
