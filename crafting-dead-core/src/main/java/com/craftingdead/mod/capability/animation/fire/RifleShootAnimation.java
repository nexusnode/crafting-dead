package com.craftingdead.mod.capability.animation.fire;

import com.craftingdead.mod.capability.animation.IAnimation;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;

public class RifleShootAnimation implements IAnimation {

  @Override
  public void apply(MatrixStack matrixStack, double progress) {
    matrixStack
        .multiply(Vector3f.POSITIVE_X
            .getDegreesQuaternion((float) Math.sin(Math.toRadians(progress * 180))));
    matrixStack.translate(0, 0, (float) (Math.sin(Math.toRadians(progress * 180)) / 15F));
  }

  @Override
  public float getLength() {
    return 125F;
  }
}
