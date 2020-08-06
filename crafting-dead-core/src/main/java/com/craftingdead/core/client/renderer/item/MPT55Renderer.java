package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.ModelM4A1IS1;
import com.craftingdead.core.client.renderer.item.model.ModelM4A1IS2;
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

public class MPT55Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelM4A1IS1();
  private final Model ironSight2 = new ModelM4A1IS2();

  public MPT55Renderer() {
    super(ModItems.MPT55);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(1.8F, -0.4F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15));
    matrixStack.translate(-0.2F, 0.55F, 0.0F);

    float scale = 0.3F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.5F;
    this.muzzleFlashY = -0.2F;
    this.muzzleFlashZ = -1.85F;
    this.muzzleScale = 2F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-33));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(1.5F, -0.25F, 0.27F);

    float scale = 0.25F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-35));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(3.6F, 0.3F, 0.95F);

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(10));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(-0.4F));
    matrixStack.translate(-0.6F, 0F, 0F);
    matrixStack.translate(0F, 0.033F, 0F);
  }

  @Override
  protected void renderAdditionalParts(LivingEntity livingEntity, IGun gun, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    this.renderIronSight1(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    this.renderIronSight2(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  @Override
  protected void applyWearingTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(90));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(90));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
    float scale = 0.18F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(0.8F, 1.3F, 1F);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
      float scale = 1.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(3F, -0.27F, -0.02F);
      scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-0.5F, -0.04F, -0.00F);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m4a1_is1.png")));
      this.ironSight1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.translate(-0.6F, -0.46F, -0.056F);
      float scale = 1F;
      matrixStack.scale(scale, scale, scale);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/m4a1_is2.png")));
      this.ironSight2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {

    if (attachmentItem == ModItems.LP_SCOPE.get()) {
      matrixStack.translate(-38D, -8D, 0.7D);
      float scale = 1.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-40D, -4.6D, -0.9D);
      float scale = 3.2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.ACOG_SIGHT.get()) {

      matrixStack.translate(-38D, -5D, 0.7D);
      float scale = 1.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(25D, -0.5D, 1.5D);
      float scale = 2.4F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(9D, -1.25D, 1.5D);
      float scale = 1F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.BIPOD.get()) {
      matrixStack.translate(7D, -1.2D, 0.2D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(PlayerEntity playerEntity, IGun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(0.03F, 0.15F, -0.05F);
    }
  }

  @Override
  protected void applySprintingTransforms(MatrixStack matrixStack) {
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-70));
    matrixStack.translate(4.3F, 0.0F, 3F);
  }
}

