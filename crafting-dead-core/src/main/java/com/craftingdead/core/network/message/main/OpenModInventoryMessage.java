package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.player.ServerPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenModInventoryMessage {

  public static void encode(OpenModInventoryMessage msg, PacketBuffer out) {}

  public static OpenModInventoryMessage decode(PacketBuffer in) {
    return new OpenModInventoryMessage();
  }

  public static boolean handle(OpenModInventoryMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx
        .get()
        .getSender()
        .getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof ServerPlayer)
        .map(living -> (ServerPlayer) living)
        .ifPresent(ServerPlayer::openInventory);
    return true;
  }
}
