package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.immerse.client.util.IFramebufferResizeListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

  @Inject(method = "updateShaderGroupSize", at = @At("RETURN"))
  private void updateShaderGroupSize(int framebufferWidth, int framebufferHeight,
      CallbackInfo callbackInfo) {
    final Minecraft mc = Minecraft.getInstance();
    if (mc.currentScreen instanceof IFramebufferResizeListener) {
      ((IFramebufferResizeListener) mc.currentScreen).framebufferResized();
    }
  }
}
