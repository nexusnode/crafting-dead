package com.craftingdead.core.network.message.play;

import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class SyncLivingMessage {
  private int entityId;
  private Map<Integer, ItemStack> slotMap;


  public SyncLivingMessage(int entityId, Map<Integer, ItemStack> slotMap) {
    this.entityId = entityId;
    this.slotMap = slotMap;
  }

  public static void encode(SyncLivingMessage msg, PacketBuffer out) {
    out.writeInt(msg.entityId);
    out.writeInt(msg.slotMap.size());
    for (Map.Entry<Integer, ItemStack> slotEntry : msg.slotMap.entrySet()) {
      out.writeInt(slotEntry.getKey());
      out.writeItemStack(slotEntry.getValue());
    }
  }

  public static SyncLivingMessage decode(PacketBuffer in) {
    int entityId = in.readInt();
    int size = in.readInt();
    Map<Integer, ItemStack> stackMap = size > 0 ? new HashMap<>(size) : Collections.emptyMap();
    for (int i = 0; i < size; i++) {
      int slotId = in.readInt();
      ItemStack stackInSlot = in.readItemStack();
      stackMap.put(slotId, stackInSlot);
    }
    return new SyncLivingMessage(entityId, stackMap);
  }

  public static boolean handle(SyncLivingMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world = LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.map(world1 -> world1.getEntityByID(msg.entityId))
        .map(entity -> entity.getCapability(ModCapabilities.LIVING).orElse(null))
        .ifPresent(living -> {
          IItemHandlerModifiable itemHandler = living.getItemHandler();
          for (Map.Entry<Integer, ItemStack> slotEntry : msg.slotMap.entrySet()) {
            itemHandler.setStackInSlot(slotEntry.getKey(), slotEntry.getValue());
          }
        });
    return true;
  }
}
