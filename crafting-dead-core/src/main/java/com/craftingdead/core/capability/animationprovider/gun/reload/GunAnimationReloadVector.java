package com.craftingdead.core.capability.animationprovider.gun.reload;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.item.ItemStack;

public class GunAnimationReloadVector extends GunAnimationReload {

  @Override
  public void doRenderHand(ItemStack par1, float par2, boolean par3, MatrixStack matrixStack) {
    if (par3) {
      float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);
      matrixStack.rotate(new Vector3f(1.0F, 0.0F, 1.0F).rotationDegrees(progress * 0.2F));
    } else {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * par2;
      matrixStack.translate(-transprogress * 1F, transprogress * 0.5F, transprogress);

      float progress = (lastRotation1 + (rotation1 - lastRotation1) * par2);
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(progress * 0.2F));
    }
  }

  @Override
  public void doRender(ItemStack par1, float par2, MatrixStack matrixStack) {
    float progress = lastRotation1 + (rotation1 - lastRotation1) * par2;
    matrixStack.rotate(new Vector3f(2.0F, 0.0F, -1.0F).rotationDegrees(progress));
  }
}

