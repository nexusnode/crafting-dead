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

package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.renderer.item.model.ModelM4A1IS1;
import com.craftingdead.core.client.renderer.item.model.ModelM4A1IS2;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.attachment.Attachments;
import com.craftingdead.core.world.gun.type.GunTypes;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.item.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class MPT55Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelM4A1IS1();
  private final Model ironSight2 = new ModelM4A1IS2();

  public MPT55Renderer() {
    super(ModItems.MPT55.getId(), GunTypes.MPT55);
  }

  @Override
  protected void applyGenericTransforms(Gun gun, MatrixStack matrixStack) {
    matrixStack.scale(0.3F, 0.3F, 0.3F);
    matrixStack.translate(2.5F, -1.1F, 0);
  }

  @Override
  protected void applyThirdPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(1.8F, -0.4F, 0.35F);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(15));
    matrixStack.translate(-0.2F, 0.55F, 0.0F);

    float scale = 0.3F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.5F;
    this.muzzleFlashY = -0.2F;
    this.muzzleFlashZ = -1.85F;
    this.muzzleScale = 2F;

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-33));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(1.5F, -0.25F, 0.27F);

    float scale = 0.25F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-35.5F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5.1F));

    matrixStack.translate(3.67F, 0.315F, 0.952F);

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(10));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-0.8F));
    matrixStack.translate(-0.6F, 0F, 0F);
    matrixStack.translate(0F, 0.034F, 0F);

    if (gun.getAttachments().contains(Attachments.RED_DOT_SIGHT.get())) {
      matrixStack.translate(0F, 0.05F, -0.007F);
    } else if (gun.getAttachments().contains(Attachments.ACOG_SIGHT.get())) {
      matrixStack.translate(0F, 0.026F, -0.002F);
    }
  }

  @Override
  protected void renderAdditionalParts(Gun gun, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    this.renderIronSight1(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    this.renderIronSight2(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  @Override
  protected void applyWearingTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
    float scale = 0.18F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(0.8F, 1.3F, 1F);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
      float scale = 1.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(3F, -0.27F, -0.02F);
      scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-0.5F, -0.04F, -0.00F);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.renderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m4a1_is1.png")));
      this.ironSight1.renderToBuffer(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F,
          1.0F,
          1.0F, 1.0F);
    }
    matrixStack.popPose();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      matrixStack.translate(-0.6F, -0.445F, -0.032F);
      float scale = 1F;
      matrixStack.scale(scale, scale, scale);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.renderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m4a1_is2.png")));
      this.ironSight2.renderToBuffer(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F,
          1.0F,
          1.0F, 1.0F);
    }
    matrixStack.popPose();
  }

  @Override
  protected void applyMagazineTransforms(ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(Attachment attachment,
      MatrixStack matrixStack) {

    if (attachment == Attachments.LP_SCOPE.get()) {
      matrixStack.translate(-38D, -8D, 0.7D);
      float scale = 1.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-40D, -4.6D, -0.9D);
      float scale = 3.2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.ACOG_SIGHT.get()) {

      matrixStack.translate(-38D, -5D, 0.52D);
      float scale = 1.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.SUPPRESSOR.get()) {
      matrixStack.translate(25D, -0.5D, 1.5D);
      float scale = 2.4F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.TACTICAL_GRIP.get()) {
      matrixStack.translate(9D, -1.25D, 1.5D);
      float scale = 1F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.BIPOD.get()) {
      matrixStack.translate(7D, -1.2D, 0.2D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(Gun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(0.03F, 0.15F, -0.05F);
    }
  }

  @Override
  protected void applySprintingTransforms(MatrixStack matrixStack, float pct) {
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(pct * -50));
    matrixStack.translate(pct * 3F, 0.0F, pct * 5F);
  }
}

