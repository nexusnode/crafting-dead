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
import com.craftingdead.core.client.renderer.item.model.ModelPistolIS1;
import com.craftingdead.core.client.renderer.item.model.ModelPistolIS2;
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

public class MP5A5Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelPistolIS1();
  private final Model ironSight2 = new ModelPistolIS2();

  public MP5A5Renderer() {
    super(ModItems.MP5A5.getId(), GunTypes.MP5A5);
  }

  @Override
  protected void applyGenericTransforms(Gun gun, MatrixStack matrixStack) {
    matrixStack.scale(0.4F, 0.4F, 0.4F);
    matrixStack.translate(1.5, -0.85, 0);
  }

  @Override
  protected void applyThirdPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.translate(0.0F, 0.0F, 0.70F);

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(78));

    matrixStack.translate(0.0F, -0.0F, 0.55F);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(15));

    matrixStack.translate(0.15F, -0.05F, 0.0F);

    float scale = 0.46F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(Gun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.43F;
    this.muzzleFlashY = -0.3F;
    this.muzzleFlashZ = -1.85F;
    this.muzzleScale = 2F;

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-32));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(-2));

    matrixStack.translate(1.24F, -0.27F, 0.25F);

    float scale = 0.33F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(Gun gun,
      MatrixStack matrixStack) {
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-35));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(3.6F, 0.3F, 0.95F);

    float scale = 1.0F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(10));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-0.4F));
    matrixStack.translate(-0.6F, 0F, 0F);
    matrixStack.translate(0F, 0.028F, 0F);

    if (gun.getAttachments().contains(Attachments.RED_DOT_SIGHT.get())) {
      matrixStack.translate(0F, 0.08F, 0.00F);
    } else if (gun.getAttachments().contains(Attachments.ACOG_SIGHT.get())) {
      matrixStack.translate(0F, 0.0795F, -0.0025F);
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
      float scale = 1.3F;
      matrixStack.scale(scale, scale, scale);
      scale = 0.48F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(2.9F, -0.60F, -0.1F);
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.renderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/g18_is1.png")));
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
      matrixStack.translate(0.5F, -0.405F, 0.025F);
      float scale = 1F;
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
    float scale = 0.29F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.3F, 0.70F, 0.5F);
  }

  @Override
  protected void applyMagazineTransforms(ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(Attachment attachment,
      MatrixStack matrixStack) {

    if (attachment == Attachments.SUPPRESSOR.get()) {
      matrixStack.translate(16F, -0.4F, 2F);
      float scale = 1.5F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-23F, -3.6F, -0.4F);
      float scale = 1.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.ACOG_SIGHT.get()) {

      matrixStack.translate(-16F, -3.6F, 0.3F);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(Gun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.02F, -0.1F, -0.2F);
    } else {
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(9));
      matrixStack.translate(0.04F, 0.18F, -0.15F);
    }
  }

  @Override
  protected void applySprintingTransforms(MatrixStack matrixStack, float pct) {
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(pct * -40));
    matrixStack.translate(pct * 1F, 0.0F, pct * 2F);
  }
}
