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

import java.util.UUID;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.client.gui.screen.game.shop.ShopScreen;
import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.module.Module;
import com.craftingdead.immerse.game.module.shop.message.BuyItemMessage;
import com.craftingdead.immerse.game.module.shop.message.SyncUserMessage;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.game.network.MessageHandlerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.network.NetworkEvent;

public class ClientShopModule extends ShopModule implements Module.Tickable {

  private static final MessageHandlerRegistry<ClientShopModule> messageHandlers =
      new MessageHandlerRegistry<>();

  static {
    messageHandlers.register(SyncUserMessage.class, ClientShopModule::handleSyncUser);
  }

  private final Minecraft minecraft = Minecraft.getInstance();

  private int buyTimeSeconds;
  private int money;

  public void buyItem(UUID itemId) {
    GameNetworkChannel.sendToServer(this.getType(), new BuyItemMessage(itemId));
  }

  public int getBuyTimeSeconds() {
    return this.buyTimeSeconds;
  }

  public boolean canAfford(int amount) {
    return this.getMoney() >= amount;
  }

  public int getMoney() {
    return this.money;
  }

  private void handleSyncUser(SyncUserMessage message, NetworkEvent.Context context) {
    this.buyTimeSeconds = message.getBuyTimeSeconds();
    this.money = message.getMoney();
  }

  @Override
  public <MSG> void handleMessage(MSG message, NetworkEvent.Context context) {
    messageHandlers.handle(this, message, context);
  }

  @Override
  public void tick() {
    // Consume key event
    while (this.minecraft.options.keyInventory.consumeClick()) {
      if (!this.minecraft.player.isSpectator()) {
        PlayerExtension<?> player = PlayerExtension.getOrThrow(this.minecraft.player);
        if (this.buyTimeSeconds > 0) {
          this.minecraft.setScreen(new ShopScreen(null, this, player));
        } else {
          this.minecraft.gui.getChat().addMessage(GameUtil.formatMessage(
              new TranslatableComponent("message.buy_time_expired")));
        }
      }
    }
  }
}
