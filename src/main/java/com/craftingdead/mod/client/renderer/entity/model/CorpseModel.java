package com.craftingdead.mod.client.renderer.entity.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class CorpseModel extends Model {

  private final RendererModel bipedHead;

  private final RendererModel bipedBody;

  private final RendererModel bipedRightArm;
  private final RendererModel bipedLeftArm;

  private final RendererModel bipedRightLeg;
  private final RendererModel bipedLeftLeg;

  private final RendererModel bipedHeadwear;

  private final RendererModel bipedBodyWear;

  private final RendererModel bipedRightArmwear;
  private final RendererModel bipedLeftArmwear;

  private final RendererModel bipedRightLegwear;
  private final RendererModel bipedLeftLegwear;

  private final RendererModel bipedCape;

  private final RendererModel bipedDeadmau5Head;

  public CorpseModel(boolean smallArmsIn) {
    this.textureWidth = 64;
    this.textureHeight = 64;

    this.bipedHead = new RendererModel(this, 0, 0);
    this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
    this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedBody = new RendererModel(this, 16, 16);
    this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
    this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);

    if (smallArmsIn) {
      this.bipedLeftArm = new RendererModel(this, 32, 48);
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);

      this.bipedRightArm = new RendererModel(this, 40, 16);
      this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);

      this.bipedLeftArmwear = new RendererModel(this, 48, 48);
      this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F);
      this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);

      this.bipedRightArmwear = new RendererModel(this, 40, 32);
      this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F);
      this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
    } else {
      this.bipedLeftArm = new RendererModel(this, 32, 48);
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);

      this.bipedRightArm = new RendererModel(this, 40, 16);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);

      this.bipedLeftArmwear = new RendererModel(this, 48, 48);
      this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
      this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);

      this.bipedRightArmwear = new RendererModel(this, 40, 32);
      this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.25F);
      this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
    }

    this.bipedRightLeg = new RendererModel(this, 0, 16);
    this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);

    this.bipedLeftLeg = new RendererModel(this, 16, 48);
    this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
    this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);

    this.bipedHeadwear = new RendererModel(this, 32, 0);
    this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
    this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedBodyWear = new RendererModel(this, 16, 32);
    this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F);
    this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);

    this.bipedRightLegwear = new RendererModel(this, 0, 32);
    this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
    this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);

    this.bipedLeftLegwear = new RendererModel(this, 0, 48);
    this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F);
    this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);

    this.bipedDeadmau5Head = new RendererModel(this, 24, 0);
    this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, 0.0F);

    this.bipedCape = new RendererModel(this, 0, 0);
    this.bipedCape.setTextureSize(64, 32);
    this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, 0.0F);
  }

  public void render(int limbCount, float scale) {
    GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);

    this.bipedHead.render(scale);
    this.bipedBody.render(scale);

    if (limbCount > 3) {
      this.bipedRightArm.render(scale);
    }

    if (limbCount > 2) {
      this.bipedLeftArm.render(scale);
    }

    if (limbCount > 1) {
      this.bipedRightLeg.render(scale);
    }

    if (limbCount > 0) {
      this.bipedLeftLeg.render(scale);
    }
  }
}
