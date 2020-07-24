package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.attachment.ModelM4A1IS1;
import com.craftingdead.core.client.renderer.item.model.attachment.ModelM4A1IS2;
import com.craftingdead.core.client.renderer.item.model.gun.ModelACR;
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

public class RenderACR extends RenderGun {

  private Model ironSight1 = new ModelM4A1IS1();
  private Model ironSight2 = new ModelM4A1IS2();

  @Override
  protected void renderGunThirdPerson(LivingEntity livingEntity, IGun itemstack,
      MatrixStack matrixStack) {
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-15.0F));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(77.0F));

    matrixStack.translate(0.6F, -0.4F, 0.35F);

    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(15.0F));
    matrixStack.translate(-0.2F, 0.55F, 0.0F);
  }

  @Override
  protected void renderGunFirstPerson(PlayerEntity entityplayer, IGun itemstack,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.1F;
    this.muzzleFlashY = 0F;
    this.muzzleFlashZ = -1.8F;
    this.muzzleScale = 1.2F;

    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-33.0F));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(5.0F));
    matrixStack.translate(0.6F, -0F, 0.25F);

    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void renderGunFirstPersonAiming(PlayerEntity entityplayer, IGun itemstack,
      MatrixStack matrixStack) {

    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-35.0F));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(5.0F));
    matrixStack.translate(1F, -0.22F, 0.94F);

    float scale = 0.8F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(10.0F));
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-0.4F));
    matrixStack.translate(-0.6F, 0F, 0F);
    matrixStack.translate(0F, 0.033F, 0F);
  }

  @Override
  protected void renderIronSights(LivingEntity livingEntity, IGun itemstack,
      MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    this.renderIronSight1(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
    this.renderIronSight2(matrixStack, renderTypeBuffer, packedLight, packedOverlay);
  }

  @Override
  protected void renderGunOnPlayerBack(LivingEntity entity, IGun itemstack,
      MatrixStack matrixStack) {

    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

    float scale = 0.55F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(-0.7F, 0.6F, 0.3F);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
      float scale = 0.5F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(0.9F, -0.7F, -0.145F);
      scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-0.5F, -0.04F, -0.00F);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight1.getLayer(
          new ResourceLocation(CraftingDead.ID, "textures/models/attachments/m4a1_is1.png")));
      this.ironSight1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.translate(1.09F, -0.41F, 0.0625F);
      float scale = 0.25F;
      matrixStack.scale(scale, scale, scale);
      IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(this.ironSight2.getLayer(
          new ResourceLocation(CraftingDead.ID, "textures/models/attachments/m4a1_is2.png")));
      this.ironSight2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F, 1.0F);
    }
    matrixStack.pop();
  }

  @Override
  protected void renderGunAmmo(LivingEntity livingEntity, ItemStack itemstack,
      MatrixStack matrixStack) {

    if (itemstack.getItem() == ModItems.STANAG_20_ROUND_MAGAZINE.get()) {
      matrixStack.translate(0.8D, -1.5D, 0.2D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (itemstack.getItem() == ModItems.STANAG_30_ROUND_MAGAZINE.get()) {
      matrixStack.translate(0.8D, 0D, 0.2D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (itemstack.getItem() == ModItems.STANAG_DRUM_MAGAZINE.get()) {
      matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
      matrixStack.translate(-1.7D, -0D, 0.5D);
      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(5.0F));
    }

    if (itemstack.getItem() == ModItems.STANAG_BOX_MAGAZINE.get()) {
      matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
      matrixStack.translate(-2.4D, 1.2D, 0.5D);
      float scale = 1.2F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  protected void renderGunAttachment(LivingEntity livingEntity, AttachmentItem attachment,
      MatrixStack matrixStack) {

    if (attachment == ModItems.LP_SCOPE.get()) {
      matrixStack.translate(-3D, -5D, 0.498D);
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.HP_SCOPE.get()) {
      matrixStack.translate(-3D, -5D, 0.498D);
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-3D, -4D, 0.498D);
      float scale = 0.75F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(20D, -2.70D, 1.5D);
    }

    if (attachment == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(9D, -1.1D, 1.5D);
      float scale = 1.1F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.EOTECH_SIGHT.get()) {
      matrixStack.translate(-1.2D, -4.9D, 1D);
      float scale = 0.12F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  public void renderHandLocation(PlayerEntity entityplayer, IGun itemstack,
      boolean par3Right, MatrixStack matrixStack) {
    if (par3Right) {
      matrixStack.translate(-0.1F, -0.15F, -0.3F);
    } else {
      matrixStack.translate(0.03F, 0.15F, -0.05F);
    }
  }

  @Override
  protected Model getGunModel() {
    return new ModelACR();
  }
}
