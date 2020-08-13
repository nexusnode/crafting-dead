package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.ModelPistolIS2;
import com.craftingdead.core.client.renderer.item.model.ModelScarhIS1;
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

public class AS50Renderer extends GunRenderer {

  private final Model ironSight1 = new ModelScarhIS1();
  private final Model ironSight2 = new ModelPistolIS2();

  public AS50Renderer() {
    super(ModItems.AS50);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {
    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(77.0F));

    matrixStack.translate(0.5F, -0.75F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15));
    matrixStack.translate(-0.4F, 0.55F, 0.0F);

    float scale = 1.2F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {
    this.muzzleFlashX = 0.3F;
    this.muzzleFlashY = -0.1F;
    this.muzzleFlashZ = -1.8F;
    this.muzzleScale = 2.0F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-40.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-3.0F));

    matrixStack.translate(0.3F, -0.2F, 0.15F);

    float scale = 0.9F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(3.0F));
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-25.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5.1F));

    matrixStack.translate(-0.1F, -0.676F, 0.9705F);

    if (!gun.hasIronSight()) {
      matrixStack.translate(0.0F, 0.017F, 0.0F);
    }

    float scale = 0.6F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-0.7F));
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
      matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(1.4F, -0.53F, -0.083F);
      scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-2F, 0.6F, -0.12F);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/scarh_is1.png")));
      this.ironSight1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.translate(1.05F, -0.09F, 0.057F);
      float scale = 0.25F;
      matrixStack.scale(scale, scale + 0.5F, scale);
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

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(90.0F));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));

    float scale = 0.75F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.7F, 0.18F, 0.25F);
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {}

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {

    if (attachmentItem == ModItems.LP_SCOPE.get()) {
      matrixStack.translate(2D, -1.5D, 0.26D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
      return;
    }

    if (attachmentItem == ModItems.HP_SCOPE.get()) {
      matrixStack.translate(2D, -1.5D, 0.26D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
      return;
    }

    if (attachmentItem == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(0.4D, -0.77D, 0.26D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
      return;
    }

    if (attachmentItem == ModItems.ACOG_SIGHT.get()) {
      matrixStack.translate(1.5D, -0.78D, 0.51D);
      float scale = 0.4F;
      matrixStack.scale(scale, scale, scale);
      return;
    }

    if (attachmentItem == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(9D, 1.4D, 0.88D);
      float scale = 0.55F;
      matrixStack.scale(scale, scale, scale);
      return;
    }

    if (attachmentItem == ModItems.BIPOD.get()) {
      matrixStack.translate(9.0D, 1.5D, 0.2D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
      return;
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
    matrixStack.rotate(Vector3f.YP.rotationDegrees(-70.0F));
    matrixStack.translate(0.6F, 0.0F, -0.1F);
  }
}
