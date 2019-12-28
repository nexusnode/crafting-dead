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

  public static void handle(ReloadMessage msg, Supplier<NetworkEvent.Context> ctx) {
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
            .getCapability(ModCapabilities.SHOOTABLE)
            .ifPresent(shootable -> shootable.reload(heldStack, player.getEntity()));
      });
      ctx.get().setPacketHandled(true);
    }
  }
}
