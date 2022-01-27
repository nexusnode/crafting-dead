package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.CraftingDead;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

  @Shadow
  @Final
  private IResourceManager resourceManager;

  @Inject(method = "processLoading", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
      target = "Lnet/minecraft/client/renderer/model/ModelBakery;loadTopLevel(Lnet/minecraft/client/renderer/model/ModelResourceLocation;)V",
      ordinal = 0))
  public void prepare(IProfiler profiler, int mipLevel, CallbackInfo callbackInfo) {
    CraftingDead.getInstance().getClientDist().getItemRendererManager()
        .gatherItemRenderers(this.resourceManager, profiler)
        .forEach(this::invokeAddModelToCache);
  }

  @Invoker
  abstract void invokeAddModelToCache(ResourceLocation modelLocation);
}
