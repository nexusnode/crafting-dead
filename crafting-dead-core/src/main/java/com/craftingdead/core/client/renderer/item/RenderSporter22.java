package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.ModelM4A1IS1;
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

public class RenderSporter22 extends GunRenderer {

  private final Model ironSight1 = new ModelM4A1IS1();
  private final Model ironSight2 = new ModelPistolIS2();

  public RenderSporter22() {
    super(ModItems.SPORTER22);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(0.5F, -0.7F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15));
    matrixStack.translate(-0.5F, 0.55F, 0.0F);

    float scale = 1.0F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.1F;
    this.muzzleFlashY = 0F;
    this.muzzleFlashZ = -2.1F;
    this.muzzleScale = 1.2F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-40.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-3.0F));

    matrixStack.translate(0.4F, -0.2F, 0.2F);

    float scale = 0.75F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(3.0F));
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-24.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5.1F));

    matrixStack.translate(-0.1F, -0.677F, 0.9515F);

    if (!gun.hasIronSight()) {

      matrixStack.translate(0.0F, 0.03F, 0.0F);
    }

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-0.7F));
  }

  @Override
  protected void renderAdditionalParts(LivingEntity livingEntity, IGun gun, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
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
      matrixStack.translate(0.9F, -0.7F, -0.145F);
      scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-2.3F, 1.24F, -0.025F);

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
      matrixStack.translate(1.625F, -0.091F, 0.104F);
      float scale = 0.25F;
      matrixStack.scale(scale + 0.75F, scale, scale);
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
    matrixStack.rotate(Vector3f.YP.rotationDegrees(180));

    matrixStack.translate(-0.5F, 0.15F, 0.16F);

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {

    if (attachmentItem == ModItems.LP_SCOPE.get()) {
      matrixStack.translate(0.85D, -1.7D, 0.55D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.HP_SCOPE.get()) {
      matrixStack.translate(0.85D, -1.7D, 0.55D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(1D, -0.95D, 0.545D);
      float scale = 0.65F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.ACOG_SIGHT.get()) {

      matrixStack.translate(1.25D, -0.93D, 0.81D);
      float scale = 0.45F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(22D, -0.55D, 1.5D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(9.5D, 1.2D, 1.35D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.BIPOD.get()) {
      matrixStack.translate(7.5D, 1.2D, 0.4D);
      float scale = 0.85F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(PlayerEntity playerEntity, IGun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.1F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(0.01F, 0.15F, -0.1F);
    }
  }

  @Override
  protected void applySprintingTransforms(MatrixStack matrixStack) {
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-70));
    matrixStack.translate(0.5F, 0.0F, 0.0F);
  }
}
