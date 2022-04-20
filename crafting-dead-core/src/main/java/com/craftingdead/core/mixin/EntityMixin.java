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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.Visibility;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@Mixin(Entity.class)
public class EntityMixin {

  /**
   * Adds hook for {@link LivingExtension#isInvisible}.
   */
  @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
  private void isInvisible(CallbackInfoReturnable<Boolean> callbackInfo) {
    Entity entity = (Entity) (Object) this;
    // It's faster not flat-mapping or filtering (we want to be fast in a render method)
    entity.getCapability(LivingExtension.CAPABILITY).ifPresent(living -> {
      if (living.getVisibility() == Visibility.INVISIBLE
          || living.getVisibility() == Visibility.PARTIALLY_VISIBLE) {
        callbackInfo.setReturnValue(true);
      }
    });
  }

  /**
   * Adds hook for {@link LivingExtension#isInvisible}.
   */
  @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
  private void isInvisibleTo(Player playerEntity,
      CallbackInfoReturnable<Boolean> callbackInfo) {
    Entity entity = (Entity) (Object) this;
    // It's faster not flat-mapping or filtering (we want to be fast in a render method)
    entity.getCapability(LivingExtension.CAPABILITY).ifPresent(living -> {
      if (living.getVisibility() == Visibility.PARTIALLY_VISIBLE) {
        callbackInfo.setReturnValue(false);
      }
    });
  }
}
