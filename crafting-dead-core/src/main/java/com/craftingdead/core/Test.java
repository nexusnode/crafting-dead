package com.craftingdead.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.Synched;
import com.craftingdead.core.network.message.play.SyncGunContainerSlotMessage;
import com.craftingdead.core.world.item.gun.Gun;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

public class Test {
  
  private static final Logger logger = LogManager.getLogger();


  public static void test(AbstractContainerMenu container, ContainerSynchronizer synchronizer, CallbackInfo callbackInfo) {

    if (synchronizer == null) {
      return;
    }

    if (synchronizer.getClass().isAnonymousClass()) {
      Object parent;
      try {
        final var parentField = synchronizer.getClass().getDeclaredField("this$0");
        parentField.setAccessible(true);
        parent = parentField.get(synchronizer);
      } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
        logger.fatal("Failed to reflect", e);
        return;
      }

      if (parent instanceof ServerPlayer playerEntity) {
//        var container = (AbstractContainerMenu) (Object) this;
        for (var slot : container.slots) {
          slot.getItem().getCapability(Gun.CAPABILITY)
              .filter(Synched::requiresSync)
              .ifPresent(gun -> {
                if (slot.container == playerEntity.getInventory()) {
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
                    new SyncGunContainerSlotMessage(
                        playerEntity.getId(), slot.getSlotIndex(), gun, false));
              });
        }
      }
    }
  
  }
}
