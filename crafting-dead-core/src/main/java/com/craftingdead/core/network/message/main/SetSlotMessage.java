package com.craftingdead.core.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetSlotMessage {

  private final int entityId;
  private final int slot;
  private final ItemStack itemStack;

  public SetSlotMessage(int entityId, int slot, ItemStack itemStack) {
    this.entityId = entityId;
    this.slot = slot;
    this.itemStack = itemStack.copy();
  }

  public static void encode(SetSlotMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeVarInt(msg.slot);
    out.writeItemStack(msg.itemStack);
  }

  public static SetSlotMessage decode(PacketBuffer in) {
    return new SetSlotMessage(in.readVarInt(), in.readVarInt(), in.readItemStack());
  }

  public static boolean handle(SetSlotMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.flatMap(w -> Optional.ofNullable(w.getEntityByID(msg.entityId)))
        .filter(e -> e instanceof LivingEntity)
        .ifPresent(e -> ILiving.get((LivingEntity) e).getItemHandler().setStackInSlot(msg.slot,
            msg.itemStack));
    return true;
  }
}
