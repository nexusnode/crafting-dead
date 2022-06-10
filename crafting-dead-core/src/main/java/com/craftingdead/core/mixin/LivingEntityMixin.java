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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.SyncGunEquipmentSlotMessage;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.Gun;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

  @Inject(at = @At("RETURN"), method = "isImmobile", cancellable = true)
  private void isImmobile(CallbackInfoReturnable<Boolean> callbackInfo) {
    var self = (LivingEntity) (Object) this;
    self.getCapability(LivingExtension.CAPABILITY).ifPresent(living -> {
      if (!callbackInfo.getReturnValue() && living.isMovementBlocked()) {
        callbackInfo.setReturnValue(true);
      }
    });
  }

  // TODO - temp until https://github.com/MinecraftForge/MinecraftForge/pull/7630 gets merged
  @Redirect(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"),
      method = "collectEquipmentChanges")
  private boolean matches(ItemStack currentStack, ItemStack lastStack) {
    if (!currentStack.equals(lastStack, true)) {
      return false;
    }

    LivingEntity livingEntity = (LivingEntity) (Object) this;
    for (EquipmentSlot slotType : EquipmentSlot.values()) {
      if (currentStack == livingEntity.getItemBySlot(slotType)) {
        currentStack.getCapability(Gun.CAPABILITY)
            .filter(Gun::requiresSync)
            .ifPresent(gun -> NetworkChannel.PLAY.getSimpleChannel().send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity),
                new SyncGunEquipmentSlotMessage(livingEntity.getId(), slotType, gun, false)));
      }
    }
    return true;
  }
}
