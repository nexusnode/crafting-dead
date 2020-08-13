package com.craftingdead.core.capability.animationprovider.gun.reload;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class GunAnimationReloadMinigun extends GunAnimationReload {
  private float rotation1 = 0F;
  private float lastRotation1 = 0F;
  private float maxRotation1 = 30;

  private float trans1 = -1F;
  private float lastTrans1 = 0F;

  private boolean up = true;

  /**
   * When set to false, the clip will be loaded into the gun
   */
  public boolean ejectingClip = false;

  public GunAnimationReloadMinigun() {
    this.ejectingClip = true;
    trans1 = 0F;
  }

  public GunAnimationReloadMinigun(boolean par1) {
    this.ejectingClip = par1;
    trans1 = this.ejectingClip ? 0F : 1F;
  }

  public void setEjectingClip(boolean par1) {
    this.ejectingClip = par1;
    trans1 = this.ejectingClip ? 0F : 1F;
  }

  @Override
  public void onUpdate(Minecraft par1, LivingEntity par2, ItemStack par3, float progress) {
    if (progress * this.getMaxAnimationTick() >= 15) {
      up = false;
    }

    lastRotation1 = rotation1;
    lastTrans1 = trans1;

    if (progress * this.getMaxAnimationTick() >= 15 && progress * this.getMaxAnimationTick() <= 22) {
      return;
    }

    float roation1Speed = 3F;
    float transSpeed = 0.5F;

    if (this.ejectingClip) {
      trans1 -= transSpeed;
    } else {
      trans1 -= transSpeed;

      if (trans1 < 0) {
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

    matrixStack.rotate(Vector3f.YP.rotationDegrees(-progress));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(-progress));
    matrixStack.translate(progress / 100, 0, 0);
  }

  @Override
  public void doRenderAmmo(ItemStack par1, float par2, MatrixStack matrixStack) {
    if (!this.ejectingClip) {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(0, -transprogress, 0);
    } else {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(0, -transprogress, 0);
    }
  }

  @Override
  public void onAnimationStopped(ItemStack par1) {}

  @Override
  public float getMaxAnimationTick() {
    return 40;
  }
}
