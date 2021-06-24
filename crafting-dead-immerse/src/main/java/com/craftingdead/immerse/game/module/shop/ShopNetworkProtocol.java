/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
