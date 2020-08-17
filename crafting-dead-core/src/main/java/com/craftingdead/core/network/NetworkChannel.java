package com.craftingdead.core.network;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.network.message.main.CancelActionMessage;
import com.craftingdead.core.network.message.main.CrouchMessage;
import com.craftingdead.core.network.message.main.HitMarkerMessage;
import com.craftingdead.core.network.message.main.OpenModInventoryMessage;
import com.craftingdead.core.network.message.main.OpenStorageMessage;
import com.craftingdead.core.network.message.main.PerformActionMessage;
import com.craftingdead.core.network.message.main.SetSlotMessage;
import com.craftingdead.core.network.message.main.SyncGunMessage;
import com.craftingdead.core.network.message.main.SyncStatisticsMessage;
import com.craftingdead.core.network.message.main.ToggleFireModeMessage;
import com.craftingdead.core.network.message.main.ToggleRightMouseAbility;
import com.craftingdead.core.network.message.main.TriggerPressedMessage;
import com.craftingdead.core.network.message.main.ValidateLivingHitMessage;
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
          .messageBuilder(ToggleRightMouseAbility.class, ++id)
          .encoder(ToggleRightMouseAbility::encode)
          .decoder(ToggleRightMouseAbility::decode)
          .consumer(ToggleRightMouseAbility::handle)
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

      simpleChannel
          .messageBuilder(PerformActionMessage.class, ++id)
          .encoder(PerformActionMessage::encode)
          .decoder(PerformActionMessage::decode)
          .consumer(PerformActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(CancelActionMessage.class, ++id)
          .encoder(CancelActionMessage::encode)
          .decoder(CancelActionMessage::decode)
          .consumer(CancelActionMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(ValidateLivingHitMessage.class, ++id)
          .encoder(ValidateLivingHitMessage::encode)
          .decoder(ValidateLivingHitMessage::decode)
          .consumer(ValidateLivingHitMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(CrouchMessage.class, ++id)
          .encoder(CrouchMessage::encode)
          .decoder(CrouchMessage::decode)
          .consumer(CrouchMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(HitMarkerMessage.class, ++id)
          .encoder(HitMarkerMessage::encode)
          .decoder(HitMarkerMessage::decode)
          .consumer(HitMarkerMessage::handle)
          .add();
    }
  };

  /**
   * Network protocol version.
   */
  private static final String NETWORK_VERSION = "0.0.0.1";
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
