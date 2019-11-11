package com.craftingdead.mod.net;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.net.message.main.TriggerPressedMessage;
import com.craftingdead.mod.net.message.main.UpdateStatisticsMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public enum NetworkChannel {

  MAIN(new ResourceLocation(CraftingDead.ID, "main")) {
    @Override
    public void registerMessages(SimpleChannel simpleChannel) {
      int id = -1;

      // UpdateStatisticsMessage
      simpleChannel
          .messageBuilder(UpdateStatisticsMessage.class, id++) //
          .encoder((msg, buffer) -> {
            buffer.writeInt(msg.getDaysSurvived());
            buffer.writeInt(msg.getZombiesKilled());
            buffer.writeInt(msg.getPlayersKilled());
            buffer.writeInt(msg.getWater());
            buffer.writeInt(msg.getMaxWater());
            buffer.writeInt(msg.getStamina());
            buffer.writeInt(msg.getMaxStamina());
          }) //
          .decoder((buffer) -> {
            int daysSurvived = buffer.readInt();
            int zombiesKilled = buffer.readInt();
            int playersKilled = buffer.readInt();
            int water = buffer.readInt();
            int maxWater = buffer.readInt();
            int stamina = buffer.readInt();
            int maxStamina = buffer.readInt();
            return new UpdateStatisticsMessage(daysSurvived, zombiesKilled, playersKilled, water,
                maxWater, stamina, maxStamina);
          }) //
          .consumer((msg, ctx) -> {
            ((ClientDist) CraftingDead.getInstance().getModDist())
                .getPlayer()
                .ifPresent((player) -> player
                    .updateMetadata(msg.getDaysSurvived(), msg.getZombiesKilled(),
                        msg.getPlayersKilled(), msg.getWater(), msg.getMaxWater(), msg.getStamina(),
                        msg.getMaxStamina()));
            ctx.get().setPacketHandled(true);
          }) //
          .add();

      // TriggerPressedMessage
      simpleChannel
          .messageBuilder(TriggerPressedMessage.class, id++) //
          .encoder((msg, buffer) -> {
            buffer.writeInt(msg.getEntityId());
            buffer.writeBoolean(msg.isTriggerPressed());
          }) //
          .decoder((buffer) -> {
            int entityId = buffer.readInt();
            boolean triggerPressed = buffer.readBoolean();
            return new TriggerPressedMessage(entityId, triggerPressed);
          }) //
          .consumer((msg, ctx) -> {
            Entity entity = null;
            switch (ctx.get().getDirection().getReceptionSide()) {
              case CLIENT:
                entity = Minecraft.getInstance().world.getEntityByID(msg.getEntityId());
                break;
              case SERVER:
                entity = ctx.get().getSender();
                break;
              default:
                break;
            }
            if (entity != null) {
              entity
                  .getCapability(ModCapabilities.PLAYER, null)
                  .ifPresent((player) -> player.setTriggerPressed(msg.isTriggerPressed()));
              ctx.get().setPacketHandled(true);
            }
          }) //
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
    this.simpleChannel = NetworkRegistry.ChannelBuilder //
        .named(channelName) //
        .clientAcceptedVersions(NETWORK_VERSION::equals) //
        .serverAcceptedVersions(NETWORK_VERSION::equals) //
        .networkProtocolVersion(() -> NETWORK_VERSION) //
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
