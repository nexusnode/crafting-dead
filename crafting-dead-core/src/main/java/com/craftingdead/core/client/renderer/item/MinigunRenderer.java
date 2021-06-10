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
import com.craftingdead.core.client.renderer.item.model.ModelMinigunBarrel;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.gun.attachment.Attachment;
import com.craftingdead.core.world.gun.type.GunTypes;
import com.craftingdead.core.world.gun.type.minigun.MinigunClient;
import com.craftingdead.core.world.item.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class MinigunRenderer extends GunRenderer {

  private final Model barrelModel = new ModelMinigunBarrel();

  public MinigunRenderer() {
    super(ModItems.MINIGUN.getId(), GunTypes.MINIGUN);
  }

  @Override
  protected void applyGenericTransforms(Gun gun, MatrixStack matrixStack) {
    matrixStack.scale(1.2F, 1.2F, 1.2F);
    matrixStack.translate(-0.8F, -0.15F, 0);
  }

  @Override
  protected void applyThirdPersonTransforms(Gun gun, MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(190));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-15));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(80));

    matrixStack.translate(0F, 0.25F, 0.25F);

    matrixStack.translate(-0.8F, -0.1F, 0.0F);
    matrixStack.translate(0.3F, 0.3F, 0.2F);

    float scale = 1.4F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(Gun gun, MatrixStack matrixStack) {

    this.muzzleFlashX = 0.2F;
    this.muzzleFlashY = -0.43F;
    this.muzzleFlashZ = -1.8F;
    this.muzzleScale = 2F;

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-40.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(-3.0F));

    matrixStack.translate(0.1F, 0.5F, -0.1F);

    float scale = 1.3F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(3.0F));
  }

  @Override
  protected void applyAimingTransforms(Gun gun, MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.XP.rotationDegrees(180));
    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-24.0F));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(5.1F));

    matrixStack.translate(-0.1F, -0.665F, 0.952F);

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-0.7F));
  }

  @Override
  protected void renderAdditionalParts(Gun gun, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    if (gun.getClient() instanceof MinigunClient) {
      this.renderBarrel(((MinigunClient) gun.getClient()).getBarrelRotation(partialTicks),
          partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    }
  }

  private void renderBarrel(float rotation, float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.pushPose();
    {
      final float x = 0.155F;
      final float y = 0.035F;
      matrixStack.translate(0, x, -y);
      matrixStack.mulPose(Vector3f.XP.rotationDegrees(rotation));
      matrixStack.translate(0, -x, y);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.barrelModel
          .renderType(new ResourceLocation(CraftingDead.ID, "textures/gun/minigun.png")));
      this.barrelModel.renderToBuffer(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F,
          1.0F, 1.0F, 1.0F);
    }
    matrixStack.popPose();
  }

  @Override
  protected void applyWearingTransforms(Gun gun,
      MatrixStack matrixStack) {

    matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90));
    matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.8F, 0.2F, 0.4F);
  }

  @Override
  protected void applyMagazineTransforms(ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(Attachment attachment,
      MatrixStack matrixStack) {}

  @Override
  protected void applyHandTransforms(Gun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(0.01F, 0.15F, -0.1F);
    }
  }

  @Override
  protected void applySprintingTransforms(MatrixStack matrixStack, float pct) {
    matrixStack.mulPose(Vector3f.YP.rotationDegrees(pct * -50));
    matrixStack.translate(pct * 0.3F, 0.0F, pct * 0.1F);
  }
}
