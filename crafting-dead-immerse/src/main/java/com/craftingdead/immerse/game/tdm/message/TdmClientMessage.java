package com.craftingdead.immerse.game.tdm.message;

import com.craftingdead.immerse.game.tdm.TdmClient;
import net.minecraftforge.fml.network.NetworkEvent;

public interface TdmClientMessage {

  void handle(TdmClient gameClient, NetworkEvent.Context context);
}
