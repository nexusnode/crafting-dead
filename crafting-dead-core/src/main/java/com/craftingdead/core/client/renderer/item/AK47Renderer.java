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
import com.craftingdead.core.client.renderer.item.model.ModelAKMIS1;
import com.craftingdead.core.client.renderer.item.model.ModelAKMIS2;
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

public class AK47Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelAKMIS1();
  private final Model ironSight2 = new ModelAKMIS2();

  public AK47Renderer() {
    super(ModItems.AK47.getId(), GunTypes.AK47);
  }

  @Override
  protected void applyGenericTransforms(Gun gun, MatrixStack matrixStack) {
    matrixStack.scale(0.315F, 0.315F, 0.315F);
    matrixStack.translate(1.5, -0.85, 0);
  }

  @Override
  protected void applyThirdPersonTransforms(Gun gun,
      MatrixStack matrixStack) {
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-15));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(0.9F, -0.7F, 0.4F);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(15));

    matrixStack.translate(0.3F, 0.6F, 0.0F);

    float scale = 0.35F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(Gun gun, MatrixStack matrixStack) {
    this.muzzleFlashX = 0.1F;
    this.muzzleFlashY = -0.1F;
    this.muzzleFlashZ = -2.19F;
    this.muzzleScale = 1.2F;

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-30));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(1.8F, -0.14F, 0.1F);

    float scale = 0.35F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(Gun gun, MatrixStack matrixStack) {

    matrixStack.translate(0F, 0F, -0.002F);

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-35));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(0.6F, -0.48F, 1F);

    float scale = 0.15F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(10));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(-1));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(0.575F));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(1.25F));

    matrixStack.translate(-0.3F, -0.2F, -0.02F);

    if (gun.getAttachments().contains(Attachments.RED_DOT_SIGHT.get())) {
      matrixStack.translate(0F, 0.057F, 0.0125F);
    } else if (gun.getAttachments().contains(Attachments.ACOG_SIGHT.get())) {
      matrixStack.translate(0F, 0.059F, 0.016F);
    } else if (gun.getAttachments().contains(Attachments.EOTECH_SIGHT.get())) {
      matrixStack.translate(0F, 0.0440F, 0.014F);
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
      float scale = 1.1F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-3.2F, -0.48F, 0.02F);

      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.ironSight1.renderType(
              new ResourceLocation(CraftingDead.ID, "textures/attachment/akm_is1.png")));
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
      matrixStack.translate(0.7F, -0.59F, 0.02F);

      float scale = 0.85F;
      matrixStack.scale(scale, scale, scale);

      IVertexBuilder vertexBuilder =
          renderTypeBuffer
              .getBuffer(this.ironSight2.renderType(new ResourceLocation(CraftingDead.ID,
                  "textures/attachment/akm_is2.png")));
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

    float scale = 0.18F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(0F, 0.8F, 0.9F);
  }

  @Override
  protected void applyMagazineTransforms(ItemStack itemStack, MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(Attachment attachment,
      MatrixStack matrixStack) {

    if (attachment == Attachments.LP_SCOPE.get()) {
      matrixStack.translate(-28F, -9F, -0.35F);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.HP_SCOPE.get()) {
      matrixStack.translate(-28F, -9F, -0.35F);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-30F, -5.4F, -0.35F);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.ACOG_SIGHT.get()) {
      matrixStack.translate(-27F, -5F, 0.3F);
      float scale = 1.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.SUPPRESSOR.get()) {
      matrixStack.translate(30F, -0.2F, 2F);
      float scale = 1.9F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.TACTICAL_GRIP.get()) {
      matrixStack.translate(-3F, 3.2F, 2F);
      float scale = 3F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.BIPOD.get()) {
      matrixStack.translate(-15F, 3.7F, -2F);
      float scale = 3F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == Attachments.EOTECH_SIGHT.get()) {
      matrixStack.translate(-23D, -8.3D, 1D);
      float scale = 0.4F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  public void applyHandTransforms(Gun gun, boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.23F, -0.3F);
    } else {
      matrixStack.translate(-0.1F, 0.3F, 0.04F);
    }
  }

  @Override
  public void applySprintingTransforms(MatrixStack matrixStack, float pct) {
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(pct * -50));
    matrixStack.translate(pct * 1.75F, 0, pct * 3.5F);
  }
}
