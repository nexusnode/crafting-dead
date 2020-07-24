package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.client.ClientDist;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

  @Inject(at = @At("RETURN"), method = "renderArm")
  private void renderArm(MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
      int packedLight, AbstractClientPlayerEntity playerEntity, ModelRenderer armRenderer,
      ModelRenderer armwearRenderer,
      CallbackInfo callbackInfo) {
    final PlayerRenderer playerRenderer = (PlayerRenderer) (Object) this;
    ClientDist.renderArmsWithExtraSkins(playerRenderer, matrixStack, renderTypeBuffer, packedLight,
        playerEntity, armRenderer, armwearRenderer);
  }
}
