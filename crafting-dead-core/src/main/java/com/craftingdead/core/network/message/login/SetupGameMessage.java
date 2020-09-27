package com.craftingdead.core.network.message.login;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.game.GameType;
import com.craftingdead.core.network.NetworkChannel;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetupGameMessage extends LoginIndexedMessage {

  private final GameType gameType;

  public SetupGameMessage(GameType gameType) {
    this.gameType = gameType;
  }

  public static void encode(SetupGameMessage msg, PacketBuffer out) {
    out.writeRegistryId(msg.gameType);
  }

  public static SetupGameMessage decode(PacketBuffer in) {
    return new SetupGameMessage(in.readRegistryId());
  }

  public static void handle(SetupGameMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(
        () -> ((ClientDist) CraftingDead.getInstance().getModDist()).loadGame(msg.gameType));
    ctx.get().setPacketHandled(true);
    NetworkChannel.LOGIN.getSimpleChannel().reply(new AcknowledgeGameMessage(), ctx.get());
  }
}
