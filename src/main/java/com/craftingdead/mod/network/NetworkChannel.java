package com.craftingdead.mod.network;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.network.message.main.PlayerActionMessage;
import com.craftingdead.mod.network.message.main.SyncGunMessage;
import com.craftingdead.mod.network.message.main.SyncInventoryMessage;
import com.craftingdead.mod.network.message.main.SyncStatisticsMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public enum NetworkChannel {

  MAIN(new ResourceLocation(CraftingDead.ID, "main")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      int id = 0;

      simpleChannel
          .messageBuilder(SyncStatisticsMessage.class, ++id)
          .encoder(SyncStatisticsMessage::encode)
          .decoder(SyncStatisticsMessage::decode)
          .consumer(SyncStatisticsMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(PlayerActionMessage.class, ++id)
          .encoder(PlayerActionMessage::encode)
          .decoder(PlayerActionMessage::decode)
          .consumer(PlayerActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SyncGunMessage.class, ++id)
          .encoder(SyncGunMessage::encode)
          .decoder(SyncGunMessage::decode)
          .consumer(SyncGunMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SyncInventoryMessage.class, ++id)
          .encoder(SyncInventoryMessage::encode)
          .decoder(SyncInventoryMessage::decode)
          .consumer(SyncInventoryMessage::handle)
          .add();
    }
  };

  /**
   * Network protocol version.
   */
  private static final String NETWORK_VERSION = "0.0.1";
  /**
   * Prevents re-registering messages.
   */
  private static boolean loaded;
  /**
   * Simple channel.
   */
  private final SimpleChannel simpleChannel;

  private NetworkChannel(ResourceLocation channelName) {
    this.simpleChannel = NetworkRegistry.ChannelBuilder
        .named(channelName)
        .clientAcceptedVersions(NETWORK_VERSION::equals)
        .serverAcceptedVersions(NETWORK_VERSION::equals)
        .networkProtocolVersion(() -> NETWORK_VERSION)
        .simpleChannel();
  }

  protected abstract void registerMessages(SimpleChannel simpleChannel);

  public SimpleChannel getSimpleChannel() {
    return this.simpleChannel;
  }

  public static void loadChannels() {
    if (!loaded) {
      for (NetworkChannel channel : NetworkChannel.values()) {
        channel.registerMessages(channel.simpleChannel);
      }
      loaded = true;
    }
  }
}
