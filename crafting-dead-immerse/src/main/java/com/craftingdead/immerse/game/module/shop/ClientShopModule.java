/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.game.module.shop;

import java.util.UUID;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.immerse.client.gui.screen.game.shop.ShopScreen;
import com.craftingdead.immerse.game.GameUtil;
import com.craftingdead.immerse.game.module.GameModule;
import com.craftingdead.immerse.game.module.shop.message.BuyItemMessage;
import com.craftingdead.immerse.game.module.shop.message.SyncUserMessage;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.game.network.MessageHandlerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.network.NetworkEvent;

public class ClientShopModule extends ShopModule implements GameModule.Tickable {

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
    return this.money >= amount;
  }

  public int getMoney() {
    return this.money;
  }

  private void handleSyncUser(SyncUserMessage message, NetworkEvent.Context context) {
    this.buyTimeSeconds = message.buyTimeSeconds();
    this.money = message.money();
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
