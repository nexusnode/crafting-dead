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
import com.craftingdead.core.client.renderer.item.model.ModelPistolIS2;
import com.craftingdead.core.client.renderer.item.model.ModelScarhIS1;
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

public class MK48ModRenderer extends GunRenderer {

  private final Model ironSight1 = new ModelScarhIS1();
  private final Model ironSight2 = new ModelPistolIS2();

  public MK48ModRenderer() {
    super(ModItems.MK48MOD.getId(), GunTypes.MK48MOD);
  }

  @Override
  protected void applyGenericTransforms(Gun gun, MatrixStack matrixStack) {
    matrixStack.scale(0.65F, 0.65F, 0.65F);
    matrixStack.translate(-0.8F, -0.3F, 0);
  }

  @Override
  protected void applyThirdPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(0.6F, -0.6F, 0.4F);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(15));
    matrixStack.translate(-0.4F, 0.55F, 0.0F);

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.5F;
    this.muzzleFlashY = -0.08F;
    this.muzzleFlashZ = -1.85F;
    this.muzzleScale = 2F;

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-40.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(-3.0F));

    matrixStack.translate(0.6F, -0.3F, 0.3F);

    float scale = 0.28F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(3.0F));
  }

  @Override
  protected void applyAimingTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-24.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5.3F));

    matrixStack.translate(0.1F, -0.61F, 0.9985F);

    float scale = 0.2F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-0.7F));

    if (gun.getAttachments().contains(Attachments.RED_DOT_SIGHT.get())) {
      matrixStack.translate(0F, 0.0275F, 0.00F);
    } else if (gun.getAttachments().contains(Attachments.EOTECH_SIGHT.get())) {
      matrixStack.translate(0F, -0.0075F, -0.003F);
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
      float scale = 1.4F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(2F, -0.76F, 0.01F);
      scale = 0.7F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-2.4F, 0.6F, -0.13F);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.renderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/scarh_is1.png")));
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
      matrixStack.translate(1.63F, -0.6F, 0.04F);
      float scale = 0.8F;
      matrixStack.scale(scale, scale + 0.5F, scale);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.renderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m1911_is2.png")));
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
    float scale = 0.36F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-1F, 0.7F, 0.7F);
  }

  @Override
  protected void applyMagazineTransforms(ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(Attachment attachment,
      MatrixStack matrixStack) {

    if (attachment == Attachments.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-9D, -4.7D, -0.7D);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
      return;
    }

    if (attachment == Attachments.BIPOD.get()) {
      matrixStack.translate(8D, 2.2D, 0.3D);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
      return;
    }

    if (attachment == Attachments.SUPPRESSOR.get()) {
      matrixStack.translate(33D, -2D, 1.3D);
      float scale = 1.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.TACTICAL_GRIP.get()) {
      matrixStack.translate(12D, 2.19D, 1.5D);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.EOTECH_SIGHT.get()) {
      matrixStack.translate(-4D, -7D, 0.65D);
      float scale = 0.3F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(Gun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(0.09F, 0.15F, -0.1F);
    }
  }

  @Override
  protected void applySprintingTransforms(MatrixStack matrixStack, float pct) {
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(pct * -50));
    matrixStack.translate(pct * 1.5F, 0.0F, pct * 1.2F);
  }
}
