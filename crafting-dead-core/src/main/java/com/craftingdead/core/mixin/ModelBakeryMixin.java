/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.CraftingDead;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fml.ModLoader;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

  @Shadow
  @Final
  private ResourceManager resourceManager;

  @Inject(method = "processLoading", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
      target = "Lnet/minecraft/client/resources/model/ModelBakery;loadTopLevel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V",
      ordinal = 0))
  public void prepare(ProfilerFiller profiler, int mipLevel, CallbackInfo callbackInfo) {
    // If some mod crashed during the game initialization Crafting Dead may not be completely
    // initialized
    if (ModLoader.isLoadingStateValid()) {
      CraftingDead.getInstance().getClientDist().getItemRendererManager()
          .gatherItemRenderers(this.resourceManager, profiler).forEach(this::invokeAddModelToCache);
    }
  }

  @Invoker
  abstract void invokeAddModelToCache(ResourceLocation modelLocation);
}
