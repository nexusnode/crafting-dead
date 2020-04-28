package com.craftingdead.mod.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncInventoryMessage {

  private final int entityId;
  private final NonNullList<ItemStack> inventoryContents;

  public SyncInventoryMessage(int entityId, NonNullList<ItemStack> inventoryContents) {
    this.entityId = entityId;
    this.inventoryContents = inventoryContents;
  }

  public static void encode(SyncInventoryMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeVarInt(msg.inventoryContents.size());
    for (ItemStack itemStack : msg.inventoryContents) {
      out.writeItemStack(itemStack);
    }
  }

  public static SyncInventoryMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    int size = in.readVarInt();
    NonNullList<ItemStack> inventoryContents = NonNullList.withSize(size, ItemStack.EMPTY);
    for (int i = 0; i < size; i++) {
      inventoryContents.set(i, in.readItemStack());
    }
    return new SyncInventoryMessage(entityId, inventoryContents);
  }

  public static void handle(SyncInventoryMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world
        .map(w -> w.getEntityByID(msg.entityId))
        .flatMap(entity -> Optional
            .ofNullable(entity.getCapability(ModCapabilities.PLAYER).orElse(null)))
        .ifPresent(player -> {
          IInventory inventory = player.getInventory();
          for (int i = 0; i < msg.inventoryContents.size(); i++) {
            inventory.setInventorySlotContents(i, msg.inventoryContents.get(i));
          }
        });
    ctx.get().setPacketHandled(true);
  }
}
