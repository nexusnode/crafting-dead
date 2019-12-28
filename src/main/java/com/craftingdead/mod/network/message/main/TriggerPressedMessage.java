package com.craftingdead.mod.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class TriggerPressedMessage {

  private final int entityId;
  private final boolean triggerPressed;

  public TriggerPressedMessage(int entityId, boolean triggerPressed) {
    this.entityId = entityId;
    this.triggerPressed = triggerPressed;
  }

  public static void encode(TriggerPressedMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeBoolean(msg.triggerPressed);
  }

  public static TriggerPressedMessage decode(PacketBuffer in) {
    return new TriggerPressedMessage(in.readVarInt(), in.readBoolean());
  }

  public static void handle(TriggerPressedMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Entity entity = null;
    switch (ctx.get().getDirection().getReceptionSide()) {
      case CLIENT:
        Optional<World> world =
            LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
        entity = world.map(w -> w.getEntityByID(msg.entityId)).orElse(null);
        break;
      case SERVER:
        entity = ctx.get().getSender();
        break;
      default:
        break;
    }
    if (entity != null) {
      entity.getCapability(ModCapabilities.PLAYER).ifPresent(player -> {
        ItemStack heldStack = player.getEntity().getHeldItemMainhand();
        heldStack
            .getCapability(ModCapabilities.SHOOTABLE, null)
            .ifPresent(shootable -> shootable
                .setTriggerPressed(heldStack, player.getEntity(), msg.triggerPressed));
      });
      ctx.get().setPacketHandled(true);
    }
  }
}
