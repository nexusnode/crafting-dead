/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.SyncGunEquipmentSlotMessage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.PacketDistributor;

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

  // TODO - temp until https://github.com/MinecraftForge/MinecraftForge/pull/7630 gets merged
  @Redirect(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/item/ItemStack;areItemStacksEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"),
      method = "func_241354_r_")
  private boolean areItemStacksEqual(ItemStack currentStack, ItemStack lastStack) {
    if (!currentStack.equals(lastStack, true)) {
      return false;
    }

    LivingEntity livingEntity = (LivingEntity) (Object) this;
    for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
      if (currentStack == livingEntity.getItemStackFromSlot(slotType)) {
        currentStack.getCapability(ModCapabilities.GUN)
            .filter(IGun::requiresSync)
            .ifPresent(gun -> NetworkChannel.PLAY.getSimpleChannel().send(
                PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity),
                new SyncGunEquipmentSlotMessage(livingEntity.getEntityId(), slotType, gun, false)));
      }
    }
    return true;
  }
}
