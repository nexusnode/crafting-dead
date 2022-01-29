package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import io.sentry.Sentry;
import net.minecraft.CrashReport;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

  @Inject(method = "onServerCrash", at = @At("HEAD"))
  private void onServerCrash(CrashReport crashReport, CallbackInfo callbackInfo) {
    Sentry.captureException(crashReport.getException());
  }
}
