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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

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
              new TranslationTextComponent("message.buy_time_expired")));
        }
      }
    }
  }
}
