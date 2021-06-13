package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.world.inventory.ModEquipmentSlotType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.LivingEntity;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

  /**
   * Prevents night vision flicker due to vanilla basing the scale off of duration.
   */
  @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
  private static void getNightVisionScale(LivingEntity livingEntity, float partialTicks,
      CallbackInfoReturnable<Float> callbackInfo) {
    // It's faster not flat-mapping or filtering (we want to be fast in a render method)
    livingEntity.getCapability(Capabilities.LIVING)
        .ifPresent(l -> l.getItemHandler().getStackInSlot(ModEquipmentSlotType.HAT.getIndex())
            .getCapability(Capabilities.HAT).ifPresent(hat -> {
              if (hat.hasNightVision()) {
                callbackInfo.setReturnValue(1.0F);
              }
            }));

  }
}
