package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.living.Player;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenModInventoryMessage {

  public static void encode(OpenModInventoryMessage msg, PacketBuffer out) {}

  public static OpenModInventoryMessage decode(PacketBuffer in) {
    return new OpenModInventoryMessage();
  }

  public static boolean handle(OpenModInventoryMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Player.get(ctx.get().getSender()).openInventory();
    return true;
  }
}
