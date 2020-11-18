package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

  @Inject(at = @At("RETURN"), method = "isMovementBlocked", cancellable = true)
  private void isMovementBlocked(CallbackInfoReturnable<Boolean> callbackInfo) {
    final LivingEntity livingEntity = (LivingEntity) (Object) this;
    ILiving.getOptional(livingEntity).ifPresent(living -> {
      if (!callbackInfo.getReturnValue() && living.isMovementBlocked()) {
        callbackInfo.setReturnValue(true);
      }
    });
  }
}
