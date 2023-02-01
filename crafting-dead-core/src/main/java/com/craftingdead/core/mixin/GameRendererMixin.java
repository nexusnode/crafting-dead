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
import com.craftingdead.core.world.item.equipment.Equipment;
import com.craftingdead.core.world.item.equipment.Hat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

  /**
   * Prevents night vision flicker due to vanilla basing the scale off of duration.
   */
  @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
  private static void getNightVisionScale(LivingEntity livingEntity, float partialTicks,
      CallbackInfoReturnable<Float> callbackInfo) {
    // It's faster not flat-mapping or filtering (we want to be fast in a render method)
    livingEntity.getCapability(LivingExtension.CAPABILITY)
        .ifPresent(l -> l.getEquipmentInSlot(Equipment.Slot.HAT, Hat.class)
            .ifPresent(hat -> {
              if (hat.nightVision()) {
                callbackInfo.setReturnValue(1.0F);
              }
            }));
  }
}
