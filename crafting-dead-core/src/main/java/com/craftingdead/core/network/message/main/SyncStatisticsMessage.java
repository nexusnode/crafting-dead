package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncStatisticsMessage {

  private final int daysSurvived;
  private final int zombiesKilled;
  private final int playersKilled;
  private final int water;
  private final int maxWater;
  private final int stamina;
  private final int maxStamina;

  public SyncStatisticsMessage(int daysSurvived, int zombiesKilled, int playersKilled, int water,
      int maxWater, int stamina, int maxStamina) {
    this.daysSurvived = daysSurvived;
    this.zombiesKilled = zombiesKilled;
    this.playersKilled = playersKilled;
    this.water = water;
    this.maxWater = maxWater;
    this.stamina = stamina;
    this.maxStamina = maxStamina;
  }

  public static void encode(SyncStatisticsMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.daysSurvived);
    out.writeVarInt(msg.zombiesKilled);
    out.writeVarInt(msg.playersKilled);
    out.writeVarInt(msg.water);
    out.writeVarInt(msg.maxWater);
    out.writeVarInt(msg.stamina);
    out.writeVarInt(msg.maxStamina);
  }

  public static SyncStatisticsMessage decode(PacketBuffer in) {
    int daysSurvived = in.readVarInt();
    int zombiesKilled = in.readVarInt();
    int playersKilled = in.readVarInt();
    int water = in.readVarInt();
    int maxWater = in.readVarInt();
    int stamina = in.readVarInt();
    int maxStamina = in.readVarInt();
    return new SyncStatisticsMessage(daysSurvived, zombiesKilled, playersKilled, water, maxWater,
        stamina, maxStamina);
  }

  public static boolean handle(SyncStatisticsMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ((ClientDist) CraftingDead.getInstance().getModDist())
        .getPlayer()
        .ifPresent(player -> player
            .updateMetadata(msg.daysSurvived, msg.zombiesKilled, msg.playersKilled, msg.water,
                msg.maxWater, msg.stamina, msg.maxStamina));
    return true;
  }
}
