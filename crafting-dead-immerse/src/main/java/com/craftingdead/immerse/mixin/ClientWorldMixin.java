package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.immerse.world.IDimensionExtension;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

  @Inject(method = "getHorizonHeight", at = @At("HEAD"), cancellable = true)
  public void getHorizonHeight(CallbackInfoReturnable<Double> callbackInfo) {
    @SuppressWarnings("resource")
    ClientWorld clientWorld = (ClientWorld) (Object) this;
    if (clientWorld.getDimension() instanceof IDimensionExtension) {
      callbackInfo
          .setReturnValue(((IDimensionExtension) clientWorld.getDimension()).getHorizonHeight());
    }
  }
}
