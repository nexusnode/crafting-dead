package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MouseHandler;
import net.rocketpowered.connector.client.gui.OverlayManager;

@Mixin(MouseHandler.class)
public class MouseHelperMixin {

  @Inject(method = "onMove", at = @At("HEAD"), cancellable = true)
  private void onMove(long window, double mouseX, double mouseY, CallbackInfo callbackInfo) {
//    if (OverlayManager.INSTANCE.isVisible()) {
//      callbackInfo.cancel();
//      OverlayManager.INSTANCE.mouseMoved(mouseX, mouseY);
//    }
  }
}
