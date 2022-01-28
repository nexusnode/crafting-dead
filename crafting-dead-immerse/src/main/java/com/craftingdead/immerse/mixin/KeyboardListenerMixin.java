package com.craftingdead.immerse.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.KeyboardHandler;
import net.rocketpowered.connector.client.gui.OverlayManager;

@Mixin(KeyboardHandler.class)
public class KeyboardListenerMixin {

  @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
  public void keyPress(long window, int keyCode, int scanCode, int action, int modifiers,
      CallbackInfo callbackInfo) {
    if (action == GLFW.GLFW_PRESS) {
      if (keyCode == GLFW.GLFW_KEY_TAB
          && (modifiers & GLFW.GLFW_MOD_SHIFT) == GLFW.GLFW_MOD_SHIFT) {
        callbackInfo.cancel();
        OverlayManager.INSTANCE.toggle();
      } else if (keyCode == GLFW.GLFW_KEY_ESCAPE && OverlayManager.INSTANCE.isVisible()) {
        callbackInfo.cancel();
        OverlayManager.INSTANCE.hide();
      }
    }
  }
}
