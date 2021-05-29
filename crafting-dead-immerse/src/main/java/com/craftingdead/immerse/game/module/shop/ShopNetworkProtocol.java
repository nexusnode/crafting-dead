package com.craftingdead.immerse.game.module.shop;

import com.craftingdead.immerse.game.module.shop.message.BuyItemMessage;
import com.craftingdead.immerse.game.module.shop.message.SyncUserMessage;
import com.craftingdead.immerse.game.network.NetworkProtocol;
import com.craftingdead.immerse.game.network.SimpleNetworkProtocol;

public class ShopNetworkProtocol extends SimpleNetworkProtocol {

  public static final NetworkProtocol INSTANCE = new ShopNetworkProtocol();

  private ShopNetworkProtocol() {
    this
        .codecBuilder(0x00, BuyItemMessage.class)
        .encoder(BuyItemMessage::encode)
        .decoder(BuyItemMessage::decode)
        .register()
        .codecBuilder(0x01, SyncUserMessage.class)
        .encoder(SyncUserMessage::encode)
        .decoder(SyncUserMessage::decode)
        .register();
  }
}
