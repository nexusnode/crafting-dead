package com.craftingdead.core.client.renderer.item.gun.fire;

import com.craftingdead.core.client.renderer.item.gun.GunAnimation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class PistolShootAnimation extends GunAnimation {

  private float rotation1 = 0F;
  private float lastRotation1 = 0F;
  private float maxRotation1 = 10;

  private float trans1 = 0F;
  private float lastTrans1 = 0F;
  private float maxTrans1 = 0.5F;

  private boolean up = true;

  @Override
  public void onUpdate(Minecraft minecraft, LivingEntity livingEntity, ItemStack itemStack, float progress) {

    lastRotation1 = rotation1;
    lastTrans1 = trans1;

    float roation1Speed = 60F;
    float transSpeed = 0.3F;

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
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-rotprogress));
  }

  @Override
  public void doRenderHand(ItemStack par1, float par2, boolean par3, MatrixStack matrixStack) {
    float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
    matrixStack.translate(-transprogress, 0, 0);

    float rotprogress = lastRotation1 + (rotation1 - lastRotation1) * par2;
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-rotprogress));
  }

  public void doRenderAmmo(ItemStack par1, float par2) {}

  @Override
  public void onAnimationStopped(ItemStack par1) {}

  @Override
  public float getMaxAnimationTick() {
    return 4;
  }
}
