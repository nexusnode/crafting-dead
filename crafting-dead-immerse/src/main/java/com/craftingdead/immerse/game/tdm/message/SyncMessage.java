package com.craftingdead.immerse.game.tdm.message;

import com.craftingdead.immerse.game.tdm.TdmClient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SyncMessage implements TdmClientMessage {

  private final PacketBuffer gameData;

  public SyncMessage(PacketBuffer gameData) {
    this.gameData = gameData;
  }

  @Override
  public void handle(TdmClient gameClient, Context context) {
    gameClient.decode(this.gameData);
  }
}
