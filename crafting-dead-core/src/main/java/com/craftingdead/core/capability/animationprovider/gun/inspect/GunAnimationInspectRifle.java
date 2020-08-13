package com.craftingdead.core.capability.animationprovider.gun.inspect;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.item.ItemStack;

public class GunAnimationInspectRifle extends GunAnimationInspect {

  @Override
  public void doRenderHand(ItemStack par1, float partialTicks, boolean rightHand,
      MatrixStack matrixStack) {
    if (rightHand) {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * partialTicks;
      matrixStack.translate(transprogress * 0.4F, transprogress * 0.4F, -transprogress * 0.6F);

      float progress = (lastRotation1 + (rotation1 - lastRotation1) * partialTicks);
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(-progress * 0.1F));
    } else {
      float transprogress = lastTrans1 + (trans1 - lastTrans1) * partialTicks;
      matrixStack.translate(-transprogress, 0, -transprogress);

      float progress = (lastRotation1 + (rotation1 - lastRotation1) * partialTicks);

      matrixStack.rotate(Vector3f.YP.rotationDegrees(progress));
      matrixStack.rotate(Vector3f.XP.rotationDegrees(-progress / 2));
    }
  }
}
