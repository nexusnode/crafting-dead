package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class CrouchMessage {

  private final int entityId;
  private final boolean crouching;

  public CrouchMessage(int entityId, boolean crouching) {
    this.entityId = entityId;
    this.crouching = crouching;
  }

  public static void encode(CrouchMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeBoolean(msg.crouching);
  }

  public static CrouchMessage decode(PacketBuffer in) {
    return new CrouchMessage(in.readVarInt(), in.readBoolean());
  }

  public static boolean handle(CrouchMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> entity.getCapability(ModCapabilities.LIVING)
            .ifPresent(living -> living
                .setCrouching(msg.crouching,
                    ctx.get().getDirection().getReceptionSide().isServer())));
    return true;
  }
}
