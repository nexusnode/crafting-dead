package com.craftingdead.core.client.animation.gun;

import com.craftingdead.core.client.animation.TimedAnimation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class ReloadAnimation extends TimedAnimation {

  private static final Vector3f MUTABLE_VEC = new Vector3f();

  public ReloadAnimation(int lifetimeTicks) {
    super(lifetimeTicks);
  }

  @Override
  public void apply(float partialTicks, MatrixStack matrixStack) {
    this.applyTranslations(partialTicks, matrixStack);
  }

  @Override
  public void applyHand(Hand hand, HandSide handSide, float partialTicks, MatrixStack matrixStack) {
    this.applyTranslations(partialTicks, matrixStack);
  }

  private void applyTranslations(float partialTicks, MatrixStack matrixStack) {
    float sineProgress = MathHelper.sin(this.lerpProgress(partialTicks) * (float) Math.PI);
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(sineProgress * 30.0F));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(sineProgress * -30.0F));
    matrixStack.translate(0.0D, 0.0D, sineProgress / 4.0D);

    applyRandomMovements(MUTABLE_VEC, partialTicks);
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(MUTABLE_VEC.x()));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(MUTABLE_VEC.z()));
  }

  @Override
  public void applyCamera(float partialTicks, Vector3f rotations) {
    applyRandomMovements(rotations, partialTicks);
  }

  private static void applyRandomMovements(Vector3f vec, float partialTicks) {
    final float time = Util.getMillis() / 20.0F;
    float angle1 = MathHelper.sin(time / 30.0F);
    float angle2 = MathHelper.sin(time / 25.0F);
    float angle3 = MathHelper.sin(time / 5.0F);
    float angle4 = MathHelper.sin(time / 15.0F);
    angle1 *= angle4;
    angle2 *= angle3 * angle4;
    vec.setX(-2.0F * (angle1 * 5.0F + angle2));
    vec.setZ(2.0F * angle2);
  }
}
