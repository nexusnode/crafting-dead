package com.craftingdead.mod.client.animation.fire;

import com.craftingdead.mod.capability.animation.IAnimation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class RifleShootAnimation implements IAnimation {

  private long startTime = 0L;

  private float rotation = 0F;
  private float lastRotation = 0F;

  private float translation = 0F;
  private float lastTranslation = 0F;

  @Override
  public boolean tick() {
    if (this.startTime == 0L) {
      this.startTime = Util.milliTime();
    }
    double pct = MathHelper.clamp((Util.milliTime() - this.startTime) / 125F, 0.0F, 1.0F);

    this.lastRotation = this.rotation;
    this.lastTranslation = this.translation;

    this.rotation = (float) Math.sin(Math.toRadians(pct * 180)) / 2F;
    this.translation = (float) Math.sin(Math.toRadians(pct * 180)) / 15F;

    return pct > 1D;
  }

  @Override
  public void apply(MatrixStack matrixStack, float partialTicks) {
    float rotationProgress = MathHelper.lerp(partialTicks, this.lastRotation, this.rotation);
    matrixStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(rotationProgress));

    float translationProgress =
        MathHelper.lerp(partialTicks, this.lastTranslation, this.translation);
    matrixStack.func_227861_a_(0, 0, translationProgress);
  }
}
