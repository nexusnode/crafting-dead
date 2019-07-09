package com.craftingdead.mod.client.animation.fire;

import com.craftingdead.mod.client.animation.GunAnimation;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.item.ItemStack;

public class RifleShootAnimation implements GunAnimation {

  private int aliveTicks = 0;

  private float rotation1 = 0F;
  private float lastRotation1 = 0F;
  private float maxRotation1 = 2;

  private float trans1 = 0F;
  private float lastTrans1 = 0F;
  private float maxTrans1 = 0.1F;

  private boolean up = true;

  @Override
  public void tick() {
    this.aliveTicks++;
    this.lastRotation1 = this.rotation1;
    this.lastTrans1 = this.trans1;

    float roation1Speed = 60F;
    float transSpeed = 0.25F;

    if (this.aliveTicks == 5) {
      this.up = false;
    }

    if (this.up) {
      this.rotation1 += roation1Speed;
      this.trans1 += transSpeed;
    } else {
      this.rotation1 -= roation1Speed;
      this.trans1 -= transSpeed;
    }

    if (this.trans1 > this.maxTrans1) {
      this.trans1 = this.maxTrans1;
    }

    if (this.trans1 < 0) {
      this.trans1 = 0;
    }

    if (this.rotation1 > this.maxRotation1) {
      this.rotation1 = this.maxRotation1;
    }

    if (this.rotation1 < 0) {
      this.rotation1 = 0;
    }
  }

  @Override
  public void render(ItemStack itemStack, float partialTicks) {
    float transprogress = this.lastTrans1 + (this.trans1 - this.lastTrans1) * partialTicks;
    GlStateManager.translatef(0, 0, transprogress);

    float rotprogress = this.lastRotation1 + (this.rotation1 - this.lastRotation1) * partialTicks;
    GlStateManager.rotatef(rotprogress, 1.0F, 0.0F, 0.0F);
  }

  @Override
  public void onAnimationStopped(ItemStack itemStack) {}

  @Override
  public boolean isFinished() {
    return this.aliveTicks >= 10;
  }
}
