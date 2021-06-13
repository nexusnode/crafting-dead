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

import java.util.List;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.SyncGunContainerSlotMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.PacketDistributor;

//TODO - temp until https://github.com/MinecraftForge/MinecraftForge/pull/7630 gets merged
@Mixin(Container.class)
public class ContainerMixin {

  @Shadow
  @Final
  private List<IContainerListener> containerListeners;

  @Inject(at = @At("HEAD"), method = "broadcastChanges")
  private void broadcastChanges(CallbackInfo callbackInfo) {
    Container container = (Container) (Object) this;
    for (Slot slot : container.slots) {
      slot.getItem().getCapability(Capabilities.GUN)
          .filter(BufferSerializable::requiresSync)
          .ifPresent(gun -> {
            for (IContainerListener listener : this.containerListeners) {
              if (listener instanceof ServerPlayerEntity) {
                ServerPlayerEntity playerEntity = (ServerPlayerEntity) listener;

                if (slot.container == playerEntity.inventory) {
                  for (ItemStack equipmentStack : playerEntity.getAllSlots()) {
                    // If the item is equipment we don't need to sync it as Minecraft does
                    // that in a separate method (and if we sync it twice the capability wont think
                    // it's dirty anymore on the second call).
                    if (equipmentStack == slot.getItem()) {
                      return;
                    }
                  }
                }
                NetworkChannel.PLAY.getSimpleChannel().send(
                    PacketDistributor.PLAYER.with(() -> playerEntity),
                    new SyncGunContainerSlotMessage(playerEntity.getId(), slot.getSlotIndex(),
                        gun, false));
                break;
              }
            }
          });
    }
  }
}
