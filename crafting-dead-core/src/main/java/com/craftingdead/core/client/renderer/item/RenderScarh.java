package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.ModelScarhIS1;
import com.craftingdead.core.client.renderer.item.model.ModelScarhIS2;
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

public class RenderScarh extends GunRenderer {

  private final Model ironSight1 = new ModelScarhIS1();
  private final Model ironSight2 = new ModelScarhIS2();

  public RenderScarh() {
    super(ModItems.SCARH);
  }

  @Override
  protected void applyThirdPersonTransforms(LivingEntity livingEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-15.0F));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(77));

    matrixStack.translate(0.6F, -0.7F, 0.35F);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(15));
    matrixStack.translate(-0.45F, 0.5F, 0.0F);

    float scale = 1.2F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void applyFirstPersonTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.1F;
    this.muzzleFlashY = 0F;
    this.muzzleFlashZ = -1.8F;
    this.muzzleScale = 1.2F;

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-35));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(0.3F, -0.3F, 0.4F);

    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.XP.rotationDegrees(-2));
  }

  @Override
  protected void applyAimingTransforms(PlayerEntity playerEntity, IGun gun,
      MatrixStack matrixStack) {

    matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(-35));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(5));

    matrixStack.translate(1F, -0.22F, 0.94F);

    float scale = 1F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.rotate(Vector3f.ZP.rotationDegrees(10));
    matrixStack.rotate(Vector3f.XP.rotationDegrees(-1));
    matrixStack.rotate(Vector3f.YP.rotationDegrees(0.18F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(1.2F));
    matrixStack.translate(-0.8F, -0.17F, 0.01F);
    matrixStack.translate(0F, 0F, 0F);
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
      matrixStack.translate(-0.2F, -0.15F, -0.187F);
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
      matrixStack.translate(1.1255F, -0.19F, 0.0318F);
      float scale = 0.495F;
      matrixStack.scale(scale, scale, scale);

      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.getRenderType(
          new ResourceLocation(CraftingDead.ID, "textures/attachment/scarh_is2.png")));
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
    float scale = 0.72F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.7F, 0.2F, 0.2F);
  }

  @Override
  protected void applyMagazineTransforms(LivingEntity livingEntity, ItemStack itemStack,
      MatrixStack matrixStack) {

    if (itemStack.getItem() == ModItems.STANAG_20_ROUND_MAGAZINE.get()) {
      matrixStack.translate(3D, 1.5D, 0.05D);
      float scale = 0.9F;
      matrixStack.scale(scale, scale, scale);
    }

    if (itemStack.getItem() == ModItems.STANAG_30_ROUND_MAGAZINE.get()) {
      matrixStack.translate(3D, 2.5D, 0.05D);
      float scale = 0.9F;
      matrixStack.scale(scale, scale, scale);
    }

    if (itemStack.getItem() == ModItems.STANAG_DRUM_MAGAZINE.get()) {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
      matrixStack.translate(-1.24D, 2.4D, 2.7D);
      float scale = 0.65F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.rotate(Vector3f.XP.rotationDegrees(5));
    }

    if (itemStack.getItem() == ModItems.STANAG_BOX_MAGAZINE.get()) {
      matrixStack.rotate(Vector3f.YP.rotationDegrees(90));
      matrixStack.translate(-1.8D, 3.2D, 3D);
      float scale = 0.95F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void applyAttachmentTransforms(LivingEntity livingEntity, AttachmentItem attachmentItem,
      MatrixStack matrixStack) {

    if (attachmentItem == ModItems.LP_SCOPE.get()) {
      matrixStack.translate(1D, -1.8D, 0.25D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.HP_SCOPE.get()) {
      matrixStack.translate(1D, -1.8D, 0.25D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(0.5D, -1.02D, 0.26D);
      float scale = 0.6F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.ACOG_SIGHT.get()) {

      matrixStack.translate(1.5D, -1.035D, 0.508D);
      float scale = 0.4F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(18.3D, 0.1D, 1.15D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(9D, 1.3D, 1.05D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.BIPOD.get()) {
      matrixStack.translate(8D, 1.3D, 0.15D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachmentItem == ModItems.EOTECH_SIGHT.get()) {
      matrixStack.translate(2D, -1.8D, 0.65D);
      float scale = 0.1F;
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
    matrixStack.translate(0.7F, 0.0F, 0.2F);
  }
}
