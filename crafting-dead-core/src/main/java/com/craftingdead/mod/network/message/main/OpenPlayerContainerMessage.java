package com.craftingdead.mod.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.player.IPlayer;
import com.craftingdead.mod.network.util.NetworkUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenPlayerContainerMessage {

  private final int entityId;

  public OpenPlayerContainerMessage(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(OpenPlayerContainerMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static OpenPlayerContainerMessage decode(PacketBuffer in) {
    return new OpenPlayerContainerMessage(in.readVarInt());
  }

  public static void handle(OpenPlayerContainerMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .flatMap(entity -> Optional
            .ofNullable(entity.getCapability(ModCapabilities.LIVING).orElse(null)))
        .filter(living -> living instanceof IPlayer)
        .map(living -> (IPlayer<?>) living)
        .ifPresent(IPlayer::openPlayerContainer);
    ctx.get().setPacketHandled(true);
  }
}
