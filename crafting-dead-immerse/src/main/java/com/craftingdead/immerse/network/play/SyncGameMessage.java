package com.craftingdead.immerse.network.play;

import java.util.function.Supplier;
import com.craftingdead.immerse.CraftingDeadImmerse;
import com.craftingdead.immerse.game.ClientGameWrapper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncGameMessage {

  private final PacketBuffer data;

  public SyncGameMessage(PacketBuffer data) {
    this.data = data;
  }

  public void encode(PacketBuffer out) {
    out.writeBytes(this.data);
  }

  public static SyncGameMessage decode(PacketBuffer in) {
    return new SyncGameMessage(in);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ClientGameWrapper gameWrapper =
        CraftingDeadImmerse.getInstance().getClientDist().getGameWrapper();
    if (gameWrapper != null) {
      gameWrapper.decode(this.data);
    }
    return true;
  }
}
