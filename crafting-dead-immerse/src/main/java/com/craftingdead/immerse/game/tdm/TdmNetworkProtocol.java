package com.craftingdead.immerse.game.tdm;

import com.craftingdead.immerse.game.network.SimpleNetworkProtocol;
import com.craftingdead.immerse.game.tdm.message.RequestJoinTeamMessage;

public class TdmNetworkProtocol extends SimpleNetworkProtocol {

  public static final TdmNetworkProtocol INSTANCE = new TdmNetworkProtocol();

  private TdmNetworkProtocol() {
    this.codecBuilder(0x00, RequestJoinTeamMessage.class)
        .encoder(RequestJoinTeamMessage::encode)
        .decoder(RequestJoinTeamMessage::decode)
        .register();
  }
}
