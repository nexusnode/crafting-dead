/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.survival.client.model;

import com.craftingdead.survival.world.entity.monster.AdvancedZombieEntity;
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

  public AdvancedZombieModel(float scale, boolean child) {
    super(scale, child);
    this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
    this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
    this.bipedLeftArmwear.setPos(5.0F, 2.0F, 0.0F);
    this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
    this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
    this.bipedRightArmwear.setPos(-5.0F, 2.0F, 10.0F);
    this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
    this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
    this.bipedLeftLegwear.setPos(1.9F, 12.0F, 0.0F);
    this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
    this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
    this.bipedRightLegwear.setPos(-1.9F, 12.0F, 0.0F);
    this.bipedBodyWear = new ModelRenderer(this, 16, 32);
    this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale + 0.25F);
    this.bipedBodyWear.setPos(0.0F, 0.0F, 0.0F);
  }

  @Override
  protected Iterable<ModelRenderer> bodyParts() {
    return Iterables.concat(super.bodyParts(),
        ImmutableList.of(
            this.bipedLeftLegwear,
            this.bipedRightLegwear,
            this.bipedLeftArmwear,
            this.bipedRightArmwear,
            this.bipedBodyWear));
  }

  @Override
  public void setupAnim(T p_225597_1_, float p_225597_2_, float p_225597_3_,
      float p_225597_4_, float p_225597_5_, float p_225597_6_) {
    super.setupAnim(p_225597_1_, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
    this.bipedLeftLegwear.copyFrom(this.leftLeg);
    this.bipedRightLegwear.copyFrom(this.rightLeg);
    this.bipedLeftArmwear.copyFrom(this.leftArm);
    this.bipedRightArmwear.copyFrom(this.rightArm);
    this.bipedBodyWear.copyFrom(this.body);
  }

  @Override
  public void setAllVisible(boolean p_178719_1_) {
    super.setAllVisible(p_178719_1_);
    this.bipedLeftArmwear.visible = p_178719_1_;
    this.bipedRightArmwear.visible = p_178719_1_;
    this.bipedLeftLegwear.visible = p_178719_1_;
    this.bipedRightLegwear.visible = p_178719_1_;
    this.bipedBodyWear.visible = p_178719_1_;
  }
}
