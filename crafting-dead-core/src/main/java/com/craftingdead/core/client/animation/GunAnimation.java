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

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public abstract class GunAnimation {

  public static final String BODY = "body";
  public static final String LEFT_HAND = "left_hand";
  public static final String RIGHT_HAND = "right_hand";
  public static final String MAGAZINE = "magazine";

  /**
   * A 20 tick update to keep things neat
   */
  public abstract void onUpdate(Minecraft par1, LivingEntity par2, ItemStack par3, float progress);

  /**
   * Called to render the animation right before the gun model is rendered
   */
  public abstract void doRender(ItemStack par1, float par2, MatrixStack matrixStack);

  /**
   * Called to render the animation right before the gun model is rendered
   */
  public abstract void doRenderHand(ItemStack par1, float par2, boolean par3,
      MatrixStack matrixStack);

  public void doRenderAmmo(ItemStack itemStack, float partialTicks,
      MatrixStack matrixStack) {}

  /**
   * Determines if a specific hand should be rendered or not
   */
  public boolean renderHand(boolean par1) {
    return true;
  }

  /**
   * Fired when the animation is called to stop or just override
   */
  public abstract void onAnimationStopped(ItemStack par1);

  /**
   * Get the max amount of time this animation will be rendering in tick
   */
  protected abstract float getMaxAnimationTick();

  protected boolean isAcceptedTransformType(ItemCameraTransforms.TransformType transformType) {
    return true;
  }

  protected void applyTransforms(LivingEntity livingEntity, ItemStack itemStack, String part,
      MatrixStack matrixStack, float partialTicks) {
    switch (part) {
      case BODY:
        this.doRender(itemStack, partialTicks, matrixStack);
        break;
      case LEFT_HAND:
        this.doRenderHand(itemStack, partialTicks, false, matrixStack);
        break;
      case RIGHT_HAND:
        this.doRenderHand(itemStack, partialTicks, true, matrixStack);
        break;
      case MAGAZINE:
        this.doRenderAmmo(itemStack, partialTicks, matrixStack);
        break;
      default:
        break;
    }
  }
}
