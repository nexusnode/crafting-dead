package com.craftingdead.core.network.message.login;

import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class AcknowledgeGameMessage extends LoginIndexedMessage {

  public static void encode(AcknowledgeGameMessage msg, PacketBuffer out) {}

  public static AcknowledgeGameMessage decode(PacketBuffer in) {
    return new AcknowledgeGameMessage();
  }

  public static void handle(AcknowledgeGameMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().setPacketHandled(true);
  }
}
