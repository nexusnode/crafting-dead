package com.craftingdead.mod.client.animation.fire;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import com.craftingdead.mod.client.animation.IGunAnimation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

public class SubmachineShootAnimation implements IGunAnimation {

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

    this.rotation = (float) Math.sin(Math.toRadians(pct * 180)) / 2.5F;
    this.translation = (float) Math.sin(Math.toRadians(pct * 180)) / 15F;

    return pct > 1D;
  }

  @Override
  public void apply(Matrix4f matrix, float partialTicks) {
    float rotationProgress = MathHelper.lerp(partialTicks, this.lastRotation, this.rotation);
    matrix.rotX((float) Math.toRadians(rotationProgress));

    float translationProgress =
        MathHelper.lerp(partialTicks, this.lastTranslation, this.translation);
    matrix.setTranslation(new Vector3f(0, 0, translationProgress));
  }
}
