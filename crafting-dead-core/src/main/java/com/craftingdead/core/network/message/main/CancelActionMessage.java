package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CancelActionMessage {

  private final int entityId;

  public CancelActionMessage(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(CancelActionMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static CancelActionMessage decode(PacketBuffer in) {
    return new CancelActionMessage(in.readVarInt());
  }

  public static boolean handle(CancelActionMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> entity.getCapability(ModCapabilities.LIVING)
            .ifPresent(living -> living
                .cancelAction(ctx.get().getDirection().getReceptionSide().isServer())));
    return true;
  }
}
