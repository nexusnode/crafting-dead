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
    System.out.println("test0");

  }

  public static void encode(SetupGameMessage msg, PacketBuffer out) {
    System.out.println("test1");

    out.writeRegistryId(msg.gameType);
  }

  public static SetupGameMessage decode(PacketBuffer in) {
    System.out.println("test2");

    return new SetupGameMessage(in.readRegistryId());
  }

  public static void handle(SetupGameMessage msg, Supplier<NetworkEvent.Context> ctx) {
    System.out.println("test3");
    ctx.get().enqueueWork(
        () -> ((ClientDist) CraftingDead.getInstance().getModDist()).loadGame(msg.gameType));
    ctx.get().setPacketHandled(true);
    NetworkChannel.LOGIN.getSimpleChannel().reply(new AcknowledgeGameMessage(), ctx.get());
  }
}
