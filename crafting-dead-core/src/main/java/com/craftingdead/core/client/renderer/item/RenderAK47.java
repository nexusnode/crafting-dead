package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.client.renderer.item.model.attachment.ModelAKMIS1;
import com.craftingdead.core.client.renderer.item.model.attachment.ModelAKMIS2;
import com.craftingdead.core.client.renderer.item.model.gun.ModelAK47;
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

public class RenderAK47 extends RenderGun {

  private final Model ironSight1 = new ModelAKMIS1();
  private final Model ironSight2 = new ModelAKMIS2();

  @Override
  protected void renderGunThirdPerson(LivingEntity livingEntity, IGun itemstack,
      MatrixStack matrixStack) {
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-15));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(77));

    matrixStack.translate(0.9F, -0.7F, 0.4F);

    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(15));

    matrixStack.translate(0.3F, 0.6F, 0.0F);

    float scale = 0.35F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void renderGunFirstPerson(PlayerEntity entityplayer, IGun itemstack,
      MatrixStack matrixStack) {

    this.muzzleFlashX = 0.1F;
    this.muzzleFlashY = -0.1F;
    this.muzzleFlashZ = -2.19F;
    this.muzzleScale = 1.2F;

    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-30));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(5));

    matrixStack.translate(1.8F, -0.14F, 0.1F);

    float scale = 0.35F;
    matrixStack.scale(scale, scale, scale);
  }

  @Override
  protected void renderGunFirstPersonAiming(PlayerEntity playerEntity, IGun itemstack,
      MatrixStack matrixStack) {

    matrixStack.translate(0F, 0F, -0.002F);

    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-35));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(5));

    matrixStack.translate(0.6F, -0.48F, 1F);

    float scale = 0.15F;
    matrixStack.scale(scale, scale, scale);

    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(10));
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-1));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(0.25F));
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(1.25F));

    matrixStack.translate(-0.3F, -0.2F, 0.005F);

    if (itemstack.getAttachments().contains(ModItems.RED_DOT_SIGHT.get())) {
      matrixStack.translate(0F, 0.015F, 0F);
    }

    if (itemstack.getAttachments().contains(ModItems.ACOG_SIGHT.get())) {
      matrixStack.translate(0F, 0.02F, 0F);
    }
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
    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90));
    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));

    float scale = 0.18F;
    matrixStack.scale(scale, scale, scale);
    matrixStack.translate(0F, 0.8F, 0.9F);
  }

  @Override
  protected void renderGunAmmo(LivingEntity livingEntity, ItemStack itemstack,
      MatrixStack matrixStack) {}

  @Override
  protected void renderGunAttachment(LivingEntity livingEntity, AttachmentItem attachment,
      MatrixStack matrixStack) {

    if (attachment == ModItems.LP_SCOPE.get()) {
      matrixStack.translate(-28F, -9F, -0.35F);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.HP_SCOPE.get()) {
      matrixStack.translate(-28F, -9F, -0.35F);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.RED_DOT_SIGHT.get()) {
      matrixStack.translate(-30F, -5.4F, -0.35F);
      float scale = 2F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.ACOG_SIGHT.get()) {
      matrixStack.translate(-27F, -5F, 0.3F);
      float scale = 1.8F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.SUPPRESSOR.get()) {
      matrixStack.translate(30F, -0.2F, 2F);
      float scale = 1.9F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.TACTICAL_GRIP.get()) {
      matrixStack.translate(-3F, 3.2F, 2F);
      float scale = 3F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.BIPOD.get()) {
      matrixStack.translate(-15F, 3.7F, -2F);
      float scale = 3F;
      matrixStack.scale(scale, scale, scale);
    }

    if (attachment == ModItems.EOTECH_SIGHT.get()) {
      matrixStack.translate(-23D, -8.3D, 1D);
      float scale = 0.4F;
      matrixStack.scale(scale, scale, scale);
    }
  }

  @Override
  public void renderHandLocation(PlayerEntity entityplayer, IGun itemstack,
      boolean par3Right, MatrixStack matrixStack) {
    if (par3Right) {
      matrixStack.translate(-0.1F, -0.23F, -0.3F);
    } else {
      matrixStack.translate(-0.1F, 0.3F, 0.04F);
    }
  }

  @Override
  public void renderWhileSprinting(MatrixStack matrixStack) {
    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-70));
    matrixStack.translate(4F, 0.8F, 2.5F);
  }

  private void renderIronSight1(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      float scale = 1.1F;
      matrixStack.scale(scale, scale, scale);
      matrixStack.translate(-3.2F, -0.48F, 0.02F);
      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.ironSight2.getLayer(
              new ResourceLocation(CraftingDead.ID, "textures/models/attachments/akm_is1.png")));
      this.ironSight1.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F,
          1.0F);
    }
    matrixStack.pop();
  }

  private void renderIronSight2(MatrixStack matrixStack,
      IRenderTypeBuffer renderTypeBuffer, int packedLight, int packedOverlay) {
    matrixStack.push();
    {
      matrixStack.translate(0.7F, -0.6F, 0.01F);

      float scale = 0.8F;
      matrixStack.scale(scale, scale, scale);

      IVertexBuilder vertexBuilder =
          renderTypeBuffer.getBuffer(this.ironSight2.getLayer(new ResourceLocation(CraftingDead.ID,
              "textures/models/attachments/akm_is2.png")));
      this.ironSight2.render(matrixStack, vertexBuilder, packedLight, packedOverlay, 1.0F, 1.0F,
          1.0F,
          1.0F);

    }
    matrixStack.pop();
  }

  @Override
  protected Model getGunModel() {
    return new ModelAK47();
  }
}
