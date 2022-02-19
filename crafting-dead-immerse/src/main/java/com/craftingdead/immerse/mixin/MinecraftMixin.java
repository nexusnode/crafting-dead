/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import io.sentry.Sentry;
import net.minecraft.CrashReport;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

  /**
   * Modifies window title.
   */
  @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
  private void createTitle(CallbackInfoReturnable<String> callbackInfo) {
    callbackInfo.setReturnValue(
        "Minecraft " + SharedConstants.getCurrentVersion().getName() + " - Crafting Dead");
  }

  /**
   * Increases GUI frame rate for a smoother experience.
   */
  @Inject(method = "getFramerateLimit", at = @At("HEAD"), cancellable = true)
  private void getFramerateLimit(CallbackInfoReturnable<Integer> callbackInfo) {
    callbackInfo.setReturnValue(Minecraft.getInstance().getWindow().getFramerateLimit());
  }

  @Inject(method = "crash", at = @At("HEAD"))
  private static void crash(CrashReport crashReport, CallbackInfo callbackInfo) {
    Sentry.captureException(crashReport.getException());
  }
}
