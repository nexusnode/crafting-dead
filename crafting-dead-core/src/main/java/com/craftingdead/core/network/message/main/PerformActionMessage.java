package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.action.ActionType;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PerformActionMessage {

  private final ActionType<?> actionType;
  /**
   * Ignored on server reception - assumed to be the player who sent the message.
   */
  private final int performerEntityId;
  private final int targetEntityId;

  public PerformActionMessage(ActionType<?> actionType, int performerEntityId,
      int targetEntityId) {
    this.actionType = actionType;
    this.performerEntityId = performerEntityId;
    this.targetEntityId = targetEntityId;
  }

  public static void encode(PerformActionMessage msg, PacketBuffer out) {
    out.writeRegistryId(msg.actionType);
    out.writeVarInt(msg.performerEntityId);
    out.writeVarInt(msg.targetEntityId);
  }

  public static PerformActionMessage decode(PacketBuffer in) {
    return new PerformActionMessage(in.readRegistryId(), in.readVarInt(), in.readVarInt());
  }

  public static boolean handle(PerformActionMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), msg.performerEntityId).ifPresent(performerEntity -> {
      ILiving<?> performer =
          performerEntity.getCapability(ModCapabilities.LIVING).orElse(null);
      ILiving<?> target = msg.targetEntityId == -1 ? null
          : performerEntity.getEntityWorld().getEntityByID(msg.targetEntityId)
              .getCapability(ModCapabilities.LIVING).orElse(null);
      final boolean isServer = ctx.get().getDirection().getReceptionSide().isServer();
      if (!isServer || msg.actionType.isTriggeredByClient()) {
        performer.performAction(msg.actionType.createAction(performer, target), isServer);
      }
    });
    return true;
  }
}
