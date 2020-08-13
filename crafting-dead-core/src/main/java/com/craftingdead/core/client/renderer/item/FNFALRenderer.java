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

public class FNFALRenderer extends GunRenderer {

  private final Model ironSight1 = new ModelM4A1IS1();
  private final Model ironSight2 = new ModelM4A1IS2();

  public FNFALRenderer() {
    super(ModItems.FNFAL);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(77.0F));

    matrixStack.translate(0.6F, -0.7F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15.0F));
    matrixStack.translate(-0.4F, 0.55F, 0.0F);

    float scale = 1.1F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.5F;
    this.muzzleFlashY = -0.25F;
    this.muzzleFlashZ = -1.7F;
    this.muzzleScale = 2F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-32.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5.0F));

    matrixStack.translate(0.35F, -0.1F, 0.1F);

    float scale = 1.0F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-35.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5.0F));

    matrixStack.translate(1F, -0.22F, 0.94F);

    float scale = 1.0F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(10.0F));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(-1.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(0.18F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(0.7F));

    matrixStack.translate(-0.85F, -0.18F, 0.01F);
    matrixStack.translate(0F, 0F, 0F);

    if (!gun.hasIronSight()) {
      matrixStack.translate(0.0F, 0.017F, 0.0F);
    }
  }

  @Override
  protected void renderAdditionalParts(LivingEntity livingEntity, IGun gun, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    this.renderIronSight1(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    this.renderIronSight2(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  @Override
  protected void applyWearingTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(90.0F));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-1F, 0.25F, 0.3F);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(0.9F, -0.69F, -0.145F);
      scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-1.6F, 0.75F, 0.09F);

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
      matrixStack.translate(1.11F, -0.09F, 0.032F);
      float scale = 0.25F;
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
      matrixStack.translate(1D, -2D, 0.15D);
      float scale = 0.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.HP_SCOPE.get()) {
      matrixStack.translate(1D, -2D, 0.15D);
      float scale = 0.7F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(0.7D, -1.15D, 0.25D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.ACOG_SIGHT.get()) {
      matrixStack.translate(2D, -1.07D, 0.47D);
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(17.7D, -0.5D, 1.2D);
      float scale = 0.9F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(10D, 0.8D, 1D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.BIPOD.get()) {
      matrixStack.translate(9D, 0.8D, -0D);
      float scale = 0.9F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyHandTransforms(PlayerEntity playerEntity, IGun gun,
      boolean rightHand, MatrixStack matrixStack) {
    if (rightHand) {
      matrixStack.translate(-0.15F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(-0.1F, 0.4F, -0.12F);
    }
  }
}
