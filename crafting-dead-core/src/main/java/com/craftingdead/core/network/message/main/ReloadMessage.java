package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ReloadMessage {

  private final int entityId;

  public ReloadMessage(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(ReloadMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static ReloadMessage decode(PacketBuffer in) {
    return new ReloadMessage(in.readVarInt());
  }

  public static boolean handle(ReloadMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          ItemStack heldStack = livingEntity.getHeldItemMainhand();
          heldStack
              .getCapability(ModCapabilities.GUN)
              .ifPresent(gun -> gun
                  .reload(entity, heldStack,
                      ctx.get().getDirection().getReceptionSide().isServer()));
        });
    return true;
  }
}
