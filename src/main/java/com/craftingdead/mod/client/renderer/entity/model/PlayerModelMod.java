package com.craftingdead.mod.client.renderer.entity.model;

import com.craftingdead.mod.client.ClientDist;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

/**
 * Intercepts player display.
 */

public class PlayerModelMod<T extends LivingEntity> extends PlayerModel<T> {

  public PlayerModelMod(float modelSize, boolean smallArmsIn) {
    super(modelSize, smallArmsIn);
  }

  @Override
  public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

    super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
        scaleFactor);

    float f = (float) entityIn.getMotion().lengthSquared();
    f = f / 0.2F;
    f = f * f * f;

    if (f < 1.0F) {
      f = 1.0F;
    }

    if (this.swimAnimation > 0.0F | ClientDist.CROUCH.isKeyDown() && !entityIn.isSwimming()) {
      float f7 = limbSwing % 20.0F;
      float f8 = this.swingProgress > 0.0F ? 0.0F : this.swimAnimation;
      float f10 = 15 - f7;
      if (f7 > 0.0F && f7 < 10.f) {
        this.bipedHead.rotateAngleX = this
            .func_205060_a(this.bipedHead.rotateAngleX, (-(float) Math.PI / 4F),
                this.swimAnimation);
        this.bipedLeftArm.rotateAngleZ =
            this.func_205060_a(this.bipedLeftArm.rotateAngleZ, 2.25F + f7 / 10, this.swimAnimation);
        this.bipedRightArm.rotateAngleZ =
            MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, 4.25F - f10 / 10);
      } else if (f7 > 10.F && f7 < 20.f) {
        this.bipedLeftArm.rotateAngleZ =
            this.func_205060_a(this.bipedLeftArm.rotateAngleZ, 4.25F - f7 / 10, this.swimAnimation);
        this.bipedRightArm.rotateAngleZ =
            MathHelper.lerp(f8, this.bipedRightArm.rotateAngleZ, 3.25F + f10 / 10);
      }

      this.bipedHeadwear.copyModelAngles(this.bipedHead);
    }
  }
}
