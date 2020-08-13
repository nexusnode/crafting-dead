package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.ModelPistolIS1;
import com.craftingdead.core.client.renderer.item.model.ModelPistolIS2;
import com.craftingdead.core.item.AttachmentItem;
import com.craftingdead.core.item.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class P250Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelPistolIS1();
  private final Model ironSight2 = new ModelPistolIS2();

  public P250Renderer() {
    super(ModItems.P250);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {
    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(78));

    matrixStack.translate(-0.05F, -0.3F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15));
    matrixStack.translate(0.05F, 0.0F, 0.0F);

    float scale = 1F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.4F;
    this.muzzleFlashY = -0.2F;
    this.muzzleFlashZ = -1.6F;
    this.muzzleScale = 2F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-35));


    matrixStack.translate(0.6F, -0.3F, 0.35F);

    float scale = 0.7F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-25));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(0.1F, -0.681F, 0.972F);

    if (!gun.hasIronSight()) {

      matrixStack.translate(0.0F, 0.026F, 0.0F);
    }

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void renderAdditionalParts(LivingEntity livingEntity, IGun gun, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    this.renderIronSight1(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    this.renderIronSight2(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-0.3F, -0.1F, -0.25F);
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m1911_is1.png")));
      this.ironSight1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.translate(0.57F, -0.04F, 0.07F);
      float scale = 0.25F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m1911_is2.png")));
      this.ironSight2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  @Override
  protected void applyWearingTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(90));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(90));

    float scale = 0.45F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.translate(1.7F, -0.3F, -0.7F);
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {
    if (attachmentItem == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(12.45F, 0.0F, 1.25F);
      float scale = 1F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(0.1D, -0.5D, 0.248D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(PlayerEntity playerEntity, IGun gun,
      boolean rightHand, MatrixStack matrixStack) {
    matrixStack.translate(0.02F, 0.04F, -0.12F);
    if (rightHand) {
      matrixStack.translate(0.0F, 0.0F, 0.0F);
    } else {
      matrixStack.translate(0.0F, 0.0F, 0.0F);
    }
  }
}
