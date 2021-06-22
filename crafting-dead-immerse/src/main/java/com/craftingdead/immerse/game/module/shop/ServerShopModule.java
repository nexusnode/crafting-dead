package com.craftingdead.immerse.game.module.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.combatslot.CombatSlotType;
import com.craftingdead.immerse.game.module.Module;
import com.craftingdead.immerse.game.module.ServerModule;
import com.craftingdead.immerse.game.module.shop.message.BuyItemMessage;
import com.craftingdead.immerse.game.module.shop.message.SyncUserMessage;
import com.craftingdead.immerse.game.network.GameNetworkChannel;
import com.craftingdead.immerse.game.network.MessageHandlerRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerShopModule extends ShopModule implements ServerModule, Module.Tickable {

  public static final BiConsumer<PlayerExtension<?>, ItemStack> COMBAT_PURCHASE_HANDLER =
      (player, item) -> CombatSlotType.getSlotType(item)
          .orElseThrow(() -> new IllegalStateException("Invalid item"))
          .addToInventory(item, player.getEntity().inventory, true);

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

  public void buyItem(PlayerExtension<ServerPlayerEntity> player, UUID itemId) {
    ShopItem item = this.items.get(itemId);
    if (item == null) {
      throw new IllegalArgumentException("Unknown item ID: " + itemId.toString());
    }

    ShopUser user = this.users.get(player.getEntity().getUUID());
    if (user.money >= item.getPrice() && user.buyTimeSeconds != 0) {
      user.money -= item.getPrice();
      user.sync();
      this.purchaseHandler.accept(player, item.getItemStack());
    }
  }

  public void resetBuyTime(UUID playerId) {
    ShopUser user = this.users.get(playerId);
    if (user != null) {
      user.buyTimeSeconds = this.defaultBuyTimeSeconds;
      user.sync();
    }
  }

  private void handleBuyItem(BuyItemMessage message, NetworkEvent.Context context) {
    this.buyItem(PlayerExtension.getOrThrow(context.getSender()), message.getItemId());
  }

  @Override
  public <MSG> void handleMessage(MSG message, NetworkEvent.Context context) {
    messageHandlers.handle(this, message, context);
  }

  @Override
  public void tick() {
    if (this.secondTimer++ >= 20) {
      this.secondTimer = 0;
      for (ShopUser user : this.users.values()) {
        if (user.buyTimeSeconds > 0) {
          user.buyTimeSeconds--;
          user.sync();
        }
      }
    }
  }

  @Override
  public void addPlayer(PlayerExtension<ServerPlayerEntity> player) {
    ShopUser user = new ShopUser(player.getEntity().connection.connection);
    this.users.put(player.getEntity().getUUID(), user);
    user.sync();
  }

  @Override
  public void removePlayer(PlayerExtension<ServerPlayerEntity> player) {
    this.users.remove(player.getEntity().getUUID());
  }

  private class ShopUser {

    private final NetworkManager connection;
    private int buyTimeSeconds = ServerShopModule.this.defaultBuyTimeSeconds;
    private int money;

    private ShopUser(NetworkManager connection) {
      this.connection = connection;
    }

    private void sync() {
      this.connection.send(GameNetworkChannel.toVanillaPacket(ServerShopModule.this.getType(),
          new SyncUserMessage(this.buyTimeSeconds, this.money), NetworkDirection.PLAY_TO_CLIENT));
    }
  }
}
