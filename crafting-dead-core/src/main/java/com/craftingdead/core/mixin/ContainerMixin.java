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

import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.Synched;
import com.craftingdead.core.network.message.play.SyncGunContainerSlotMessage;
import com.craftingdead.core.world.item.gun.Gun;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

//TODO - temp until https://github.com/MinecraftForge/MinecraftForge/pull/7630 gets merged
@Mixin(AbstractContainerMenu.class)
public class ContainerMixin {

  private static final Logger logger = LogManager.getLogger();

  @Shadow
  @Nullable
  private ContainerSynchronizer synchronizer;

  @Redirect(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/world/item/ItemStack;matches(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"),
      method = "synchronizeSlotToRemote")
  private boolean matches(ItemStack lastStack, ItemStack currentStack, int slotIndex,
      ItemStack __, Supplier<ItemStack> coppiedStack) {

    if (this.synchronizer == null) {
      return ItemStack.matches(currentStack, lastStack);
    }

    if (this.synchronizer.getClass().isAnonymousClass()) {
      Object parent;
      try {
        final var parentField = this.synchronizer.getClass().getDeclaredField("this$0");
        parentField.setAccessible(true);
        parent = parentField.get(this.synchronizer);
      } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
        logger.fatal("Failed to reflect", e);
        return ItemStack.matches(currentStack, lastStack);
      }

      if (parent instanceof ServerPlayer playerEntity) {
        var container = (AbstractContainerMenu) (Object) this;

        if (!currentStack.equals(lastStack, true)) {
          return false;
        }

        currentStack.getCapability(Gun.CAPABILITY)
            .filter(Synched::requiresSync)
            .ifPresent(gun -> {
              if (container == playerEntity.inventoryMenu) {
                for (ItemStack equipmentStack : playerEntity.getAllSlots()) {
                  // If the item is equipment we don't need to sync it as Minecraft does
                  // that in a separate method (and if we sync it twice the capability wont think
                  // it's dirty anymore on the second call).
                  if (equipmentStack == currentStack) {
                    return;
                  }
                }
              }
              NetworkChannel.PLAY.getSimpleChannel().send(
                  PacketDistributor.PLAYER.with(() -> playerEntity),
                  new SyncGunContainerSlotMessage(
                      playerEntity.getId(), slotIndex, gun, false));
            });
      }
    }
    return true;
  }
}
