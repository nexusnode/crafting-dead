package com.craftingdead.mod.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.network.util.NetworkUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ToggleAimingMessage {

  private final int entityId;

  public ToggleAimingMessage(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(ToggleAimingMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static ToggleAimingMessage decode(PacketBuffer in) {
    return new ToggleAimingMessage(in.readVarInt());
  }

  public static boolean handle(ToggleAimingMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .flatMap(entity -> Optional
            .ofNullable(entity.getCapability(ModCapabilities.LIVING).orElse(null)))
        .ifPresent(
            living -> living.toggleAiming(ctx.get().getDirection().getReceptionSide().isServer()));
    return true;
  }
}
