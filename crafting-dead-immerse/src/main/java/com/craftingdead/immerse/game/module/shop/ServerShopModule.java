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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.immerse.game.module.GameModule;
import com.craftingdead.immerse.game.module.ServerModule;
import com.craftingdead.immerse.game.module.shop.message.BuyItemMessage;
import com.craftingdead.immerse.game.module.shop.message.SyncUserMessage;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.game.network.MessageHandlerRegistry;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public class ServerShopModule extends ShopModule implements ServerModule, GameModule.Tickable {

  public static final BiConsumer<PlayerExtension<?>, ItemStack> COMBAT_PURCHASE_HANDLER =
      (player, item) -> CombatSlot.getSlotType(item)
          .orElseThrow(() -> new IllegalStateException("Invalid item"))
          .addToInventory(item, player.entity().getInventory(), true);

  private static final MessageHandlerRegistry<ServerShopModule> messageHandlers =
      new MessageHandlerRegistry<>();

  static {
    messageHandlers.register(BuyItemMessage.class, ServerShopModule::handleBuyItem);
  }

  private final Map<UUID, ShopUser> users = new HashMap<>();

  private final BiConsumer<PlayerExtension<?>, ItemStack> purchaseHandler;

  private final int defaultBuyTimeSeconds;

  private int secondTimer;

  public ServerShopModule(BiConsumer<PlayerExtension<?>, ItemStack> purchaseHandler,
      int defaultBuyTimeSeconds) {
    this.purchaseHandler = purchaseHandler;
    this.defaultBuyTimeSeconds = defaultBuyTimeSeconds;
  }

  public void buyItem(PlayerExtension<ServerPlayer> player, UUID itemId) {
    var item = this.items.get(itemId);
    if (item == null) {
      throw new IllegalArgumentException("Unknown item ID: " + itemId.toString());
    }

    var user = this.users.get(player.entity().getUUID());
    if (user.money >= item.price() && user.buyTimeSeconds != 0) {
      user.money -= item.price();
      user.sync();
      this.purchaseHandler.accept(player, item.itemStack());
    }
  }

  public void resetBuyTime(UUID playerId) {
    var user = this.users.get(playerId);
    if (user != null) {
      user.buyTimeSeconds = this.defaultBuyTimeSeconds;
      user.sync();
    }
  }

  private void handleBuyItem(BuyItemMessage message, NetworkEvent.Context context) {
    this.buyItem(PlayerExtension.getOrThrow(context.getSender()), message.itemId());
  }

  @Override
  public <MSG> void handleMessage(MSG message, NetworkEvent.Context context) {
    messageHandlers.handle(this, message, context);
  }

  @Override
  public void tick() {
    if (this.secondTimer++ >= 20) {
      this.secondTimer = 0;
      for (var user : this.users.values()) {
        if (user.buyTimeSeconds > 0) {
          user.buyTimeSeconds--;
          user.sync();
        }
      }
    }
  }

  @Override
  public void addPlayer(PlayerExtension<ServerPlayer> player) {
    var user = new ShopUser(player.entity().connection.getConnection());
    this.users.put(player.entity().getUUID(), user);
    user.sync();
  }

  @Override
  public void removePlayer(PlayerExtension<ServerPlayer> player) {
    this.users.remove(player.entity().getUUID());
  }

  private class ShopUser {

    private final Connection connection;
    private int buyTimeSeconds = ServerShopModule.this.defaultBuyTimeSeconds;
    private int money;

    private ShopUser(Connection connection) {
      this.connection = connection;
    }

    private void sync() {
      this.connection.send(GameNetworkChannel.toVanillaPacket(ServerShopModule.this.getType(),
          new SyncUserMessage(this.buyTimeSeconds, this.money), NetworkDirection.PLAY_TO_CLIENT));
    }
  }
}
