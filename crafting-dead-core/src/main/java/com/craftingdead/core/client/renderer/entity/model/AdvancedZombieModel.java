package com.craftingdead.core.client.renderer.entity.model;

import com.craftingdead.core.entity.monster.AdvancedZombieEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class AdvancedZombieModel<T extends AdvancedZombieEntity> extends ZombieModel<T> {

  public final ModelRenderer bipedLeftArmwear;
  public final ModelRenderer bipedRightArmwear;
  public final ModelRenderer bipedLeftLegwear;
  public final ModelRenderer bipedRightLegwear;
  public final ModelRenderer bipedBodyWear;

  public AdvancedZombieModel(float p_i1168_1_, boolean p_i1168_2_) {
    super(p_i1168_1_, p_i1168_2_);
    this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
    this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i1168_1_ + 0.25F);
    this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
    this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
    this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i1168_1_ + 0.25F);
    this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
    this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
    this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i1168_1_ + 0.25F);
    this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
    this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
    this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i1168_1_ + 0.25F);
    this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
    this.bipedBodyWear = new ModelRenderer(this, 16, 32);
    this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_i1168_1_ + 0.25F);
    this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
  }

  @Override
  protected Iterable<ModelRenderer> getBodyParts() {
    return Iterables
        .concat(super.getBodyParts(),
            ImmutableList
                .of(this.bipedLeftLegwear, this.bipedRightLegwear, this.bipedLeftArmwear,
                    this.bipedRightArmwear, this.bipedBodyWear));
  }

  @Override
  public void setRotationAngles(T p_225597_1_, float p_225597_2_, float p_225597_3_,
      float p_225597_4_,
      float p_225597_5_, float p_225597_6_) {
    super.setRotationAngles(p_225597_1_, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_,
        p_225597_6_);
    this.bipedLeftLegwear.copyModelAngles(this.bipedLeftLeg);
    this.bipedRightLegwear.copyModelAngles(this.bipedRightLeg);
    this.bipedLeftArmwear.copyModelAngles(this.bipedLeftArm);
    this.bipedRightArmwear.copyModelAngles(this.bipedRightArm);
    this.bipedBodyWear.copyModelAngles(this.bipedBody);
  }

  @Override
  public void setVisible(boolean p_178719_1_) {
    super.setVisible(p_178719_1_);
    this.bipedLeftArmwear.showModel = p_178719_1_;
    this.bipedRightArmwear.showModel = p_178719_1_;
    this.bipedLeftLegwear.showModel = p_178719_1_;
    this.bipedRightLegwear.showModel = p_178719_1_;
    this.bipedBodyWear.showModel = p_178719_1_;
  }
}
