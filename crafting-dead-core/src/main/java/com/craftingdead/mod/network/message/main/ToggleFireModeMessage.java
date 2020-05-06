package com.craftingdead.mod.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ToggleFireModeMessage {

  private final int entityId;

  public ToggleFireModeMessage(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(ToggleFireModeMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static ToggleFireModeMessage decode(PacketBuffer in) {
    return new ToggleFireModeMessage(in.readVarInt());
  }

  public static void handle(ToggleFireModeMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          ItemStack heldStack = livingEntity.getHeldItemMainhand();
          heldStack
              .getCapability(ModCapabilities.GUN)
              .ifPresent(gun -> gun
                  .toggleFireMode(entity, ctx.get().getDirection().getReceptionSide().isServer()));
        });
    ctx.get().setPacketHandled(true);
  }
}
