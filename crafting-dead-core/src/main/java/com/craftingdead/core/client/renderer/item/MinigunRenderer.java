package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.ModelMinigunBarrel;
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

public class MinigunRenderer extends GunRenderer {

  private final Model barrelModel = new ModelMinigunBarrel();

  private float rotation = 0F;

  public MinigunRenderer() {
    super(ModItems.MINIGUN);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(0.5F, -0.75F, 0.5F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-55));
    matrixStack.translate(-0.8F, -0.1F, 0.0F);
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-70));
    matrixStack.translate(0.3F, 0.3F, 0.2F);

    float scale = 1.4F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.2F;
    this.muzzleFlashY = -0.43F;
    this.muzzleFlashZ = -1.8F;
    this.muzzleScale = 2F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-40.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-3.0F));

    matrixStack.translate(0.1F, 0.5F, -0.1F);

    float scale = 1.3F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(3.0F));
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-24.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5.1F));

    matrixStack.translate(-0.1F, -0.665F, 0.952F);

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-0.7F));
  }

  @Override
  protected void renderAdditionalParts(LivingEntity livingEntity, IGun gun, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    if (gun.isPerformingRightMouseAction()) {
      this.rotation += 15.0F;
      if (this.rotation >= 360.0F) {
        this.rotation = 0.0F;
      }
    }
    this.renderBarrel(partialTicks, matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  private void renderBarrel(float partialTicks, MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      final float x = 0.155F;
      final float y = 0.035F;
      matrixStack.translate(0, x, -y);
      matrixStack.rotate(Vector3f.XP.rotationDegrees(this.rotation));
      matrixStack.translate(0, -x, y);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.barrelModel
          .getRenderType(new ResourceLocation(CraftingDead.ID, "textures/gun/minigun.png")));
      this.barrelModel.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
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
    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.8F, 0.2F, 0.4F);
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {}

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
    matrixStack.translate(0.3F, -0.3F, 0.2F);
  }
}
