package com.craftingdead.immerse.game.tdm.message;

import com.craftingdead.immerse.game.tdm.TdmServer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface TdmServerMessage {

  void handle(TdmServer gameServer, NetworkEvent.Context context);
}
