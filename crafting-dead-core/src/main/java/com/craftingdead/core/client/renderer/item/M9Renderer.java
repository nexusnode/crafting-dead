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

public class M9Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelPistolIS1();
  private final Model ironSight2 = new ModelPistolIS2();

  public M9Renderer() {
    super(ModItems.M9);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.translate(0.0F, 0.0F, -0.05F);

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(78));

    matrixStack.translate(0.0F, -0.28F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15));

    float scale = 1.1F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.6F;
    this.muzzleFlashY = -0.25F;
    this.muzzleFlashZ = -1.9F;
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

    matrixStack.translate(0.2F, -0.695F, 0.972F);

    if (!gun.hasIronSight()) {

      matrixStack.translate(0.0F, 0.02F, 0.0F);
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
      matrixStack.translate(-0.3F, -0.15F, -0.25F);
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/g18_is1.png")));
      this.ironSight1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.translate(0.6F, -0.05F, 0.07F);
      float scale = 0.25F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/g18_is2.png")));
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

    float scale = 0.55F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.translate(1.1F, -0.2F, -0.58F);
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {
    if (itemStack.getItem() == ModItems.M9_MAGAZINE.get()) {
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(5));
    }
  }

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {
    if (attachmentItem == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(12F, -0.3F, 1.2F);
      float scale = 0.9F;
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
