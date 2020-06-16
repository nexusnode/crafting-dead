package com.craftingdead.core.network;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.network.message.main.OpenModInventoryMessage;
import com.craftingdead.core.network.message.main.OpenStorageMessage;
import com.craftingdead.core.network.message.main.ReloadMessage;
import com.craftingdead.core.network.message.main.SetSlotMessage;
import com.craftingdead.core.network.message.main.SyncGunMessage;
import com.craftingdead.core.network.message.main.SyncStatisticsMessage;
import com.craftingdead.core.network.message.main.ToggleAimingMessage;
import com.craftingdead.core.network.message.main.ToggleFireModeMessage;
import com.craftingdead.core.network.message.main.TriggerPressedMessage;
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
          .messageBuilder(OpenModInventoryMessage.class, ++id)
          .encoder(OpenModInventoryMessage::encode)
          .decoder(OpenModInventoryMessage::decode)
          .consumer(OpenModInventoryMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(ReloadMessage.class, ++id)
          .encoder(ReloadMessage::encode)
          .decoder(ReloadMessage::decode)
          .consumer(ReloadMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(ToggleAimingMessage.class, ++id)
          .encoder(ToggleAimingMessage::encode)
          .decoder(ToggleAimingMessage::decode)
          .consumer(ToggleAimingMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(ToggleFireModeMessage.class, ++id)
          .encoder(ToggleFireModeMessage::encode)
          .decoder(ToggleFireModeMessage::decode)
          .consumer(ToggleFireModeMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(TriggerPressedMessage.class, ++id)
          .encoder(TriggerPressedMessage::encode)
          .decoder(TriggerPressedMessage::decode)
          .consumer(TriggerPressedMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SyncGunMessage.class, ++id)
          .encoder(SyncGunMessage::encode)
          .decoder(SyncGunMessage::decode)
          .consumer(SyncGunMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(SetSlotMessage.class, ++id)
          .encoder(SetSlotMessage::encode)
          .decoder(SetSlotMessage::decode)
          .consumer(SetSlotMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(OpenStorageMessage.class, ++id)
          .encoder(OpenStorageMessage::encode)
          .decoder(OpenStorageMessage::decode)
          .consumer(OpenStorageMessage::handle)
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
