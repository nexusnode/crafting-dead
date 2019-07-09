package com.craftingdead.mod.client.animation.fire;

import com.craftingdead.mod.client.animation.GunAnimation;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.item.ItemStack;

public class PistolShootAnimation implements GunAnimation {

  private int aliveTicks = 0;

  private float rotation = 0F;
  private float lastRotation = 0F;
  private float maxRotation = 15;

  private float translation = 0F;
  private float lastTranslation = 0F;
  private float maxTranslation = 0.1F;

  private boolean up = true;

  @Override
  public void tick() {
    this.aliveTicks++;
    this.lastRotation = this.rotation;
    this.lastTranslation = this.translation;

    float roationSpeed = 60F;
    float translationSpeed = 0.3F;

    if (this.aliveTicks == 5) {
      this.up = false;
    }

    if (this.up) {
      this.rotation += roationSpeed;
      this.translation += translationSpeed;
    } else {
      this.rotation -= roationSpeed;
      this.translation -= translationSpeed;
    }

    if (this.translation > this.maxTranslation) {
      this.translation = this.maxTranslation;
    }

    if (this.translation < 0) {
      this.translation = 0;
    }

    if (this.rotation > this.maxRotation) {
      this.rotation = this.maxRotation;
    }

    if (this.rotation < 0) {
      this.rotation = 0;
    }
  }

  @Override
  public void render(ItemStack itemStack, float partialTicks) {
    float transprogress =
        this.lastTranslation + (this.translation - this.lastTranslation) * partialTicks;
    GlStateManager.translatef(0, 0, transprogress);

    float rotprogress = this.lastRotation + (this.rotation - this.lastRotation) * partialTicks;
    GlStateManager.rotatef(rotprogress, 1.0F, 0.0F, 0.0F);
  }

  @Override
  public void onAnimationStopped(ItemStack itemStack) {}

  @Override
  public boolean isFinished() {
    return this.aliveTicks >= 10;
  }
}
