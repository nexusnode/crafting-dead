package com.craftingdead.mod.network;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.network.message.main.PlayerActionMessage;
import com.craftingdead.mod.network.message.main.UpdateStatisticsMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public enum NetworkChannel {

  MAIN(new ResourceLocation(CraftingDead.ID, "main")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      int id = 0;

      simpleChannel
          .messageBuilder(UpdateStatisticsMessage.class, ++id)
          .encoder(UpdateStatisticsMessage::encode)
          .decoder(UpdateStatisticsMessage::decode)
          .consumer(UpdateStatisticsMessage::handle)
          .add();

      simpleChannel
          .messageBuilder(PlayerActionMessage.class, ++id)
          .encoder(PlayerActionMessage::encode)
          .decoder(PlayerActionMessage::decode)
          .consumer(PlayerActionMessage::handle)
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
