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

public class HK417Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelM4A1IS1();
  private final Model ironSight2 = new ModelM4A1IS2();

  public HK417Renderer() {
    super(ModItems.HK417.getId(), GunTypes.HK417);
  }

  @Override
  protected void applyGenericTransforms(Gun gun, MatrixStack matrixStack) {
    matrixStack.scale(1.4F, 1.4F, 1.4F);
    matrixStack.translate(-0.3, -0.2, 0);
  }

  @Override
  protected void applyThirdPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(0.7F, -0.7F, 0.4F);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(15));

    matrixStack.translate(-0.2F, 0.55F, 0.0F);

    float scale = 1.0F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.5F;
    this.muzzleFlashY = -0.15F;
    this.muzzleFlashZ = -2.3F;
    this.muzzleScale = 2F;

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-33.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5.0F));

    matrixStack.translate(0.8F, -0.26F, 0.3F);

    float scale = 1.0F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-35.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(1.6F, -0.3025F, 0.9855F);

    float scale = 1.1F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(10));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-0.4F));

    matrixStack.translate(-0.6F, 0F, 0F);
    matrixStack.translate(0F, 0.033F, 0F);

    if (gun.getAttachments().contains(Attachments.RED_DOT_SIGHT.get())) {
      matrixStack.translate(0F, 0.0115F, -0.002F);
    } else if (gun.getAttachments().contains(Attachments.ACOG_SIGHT.get())) {
      matrixStack.translate(0F, 0.00835F, -0.00075F);
    } else if (gun.getAttachments().contains(Attachments.EOTECH_SIGHT.get())) {
      matrixStack.translate(0F, 0.002F, -0.0015F);
    }
  }

  @Override
  protected void renderAdditionalParts(Gun gun, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    this.renderIronSight1(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    this.renderIronSight2(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));

      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(0.3F, -0.21F, 0.22F);
      scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-0.1F, 0.05F, -0.31F);

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
      matrixStack.translate(0.65F, -0.1075F, -0.002F);
      float scale = 0.25F;
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
  protected void applyWearingTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));

    float scale = 0.65F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.6F, 0.32F, 0.28F);
  }

  @Override
  protected void applyMagazineTransforms(ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(Attachment attachment,
      MatrixStack matrixStack) {
    if (attachment == Attachments.LP_SCOPE.get()) {
      matrixStack.translate(-3D, -2.2D, 0.1D);
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.HP_SCOPE.get()) {
      matrixStack.translate(-3D, -2.2D, 0.1D);
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-4D, -1.1D, -0.15D);
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.ACOG_SIGHT.get()) {
      matrixStack.translate(-2.3D, -1.1D, 0.15D);
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.SUPPRESSOR.get()) {
      matrixStack.translate(18D, -0.1D, 0.67D);
      float scale = 0.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.TACTICAL_GRIP.get()) {
      matrixStack.translate(6D, 0.8D, 0.8D);
      float scale = 1F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.EOTECH_SIGHT.get()) {
      matrixStack.translate(-1.2D, -2D, 0.35D);
      float scale = 0.11F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(Gun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.15F, -0.4F);
    } else {
      matrixStack.translate(0F, 0.19F, -0.1F);
    }
  }
}
