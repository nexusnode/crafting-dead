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
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

  @Shadow
  @Final
  private ResourceManager resourceManager;

  @Inject(method = "processLoading", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
      target = "Lnet/minecraft/client/resources/model/ModelBakery;loadTopLevel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V",
      ordinal = 0))
  public void prepare(ProfilerFiller profiler, int mipLevel, CallbackInfo callbackInfo) {
    CraftingDead.getInstance().getClientDist().getItemRendererManager()
        .gatherItemRenderers(this.resourceManager, profiler)
        .forEach(this::invokeAddModelToCache);
  }

  @Invoker
  abstract void invokeAddModelToCache(ResourceLocation modelLocation);
}
