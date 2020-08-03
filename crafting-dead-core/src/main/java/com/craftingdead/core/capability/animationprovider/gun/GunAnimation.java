package com.craftingdead.core.capability.animationprovider.gun;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
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

  /**
   * Get animation length in milliseconds.
   * 
   * @return length in milliseconds
   */
  public float getLength() {
    return ((this.getMaxAnimationTick() / 20) * 1000) - 50;
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
