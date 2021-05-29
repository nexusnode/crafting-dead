package com.craftingdead.immerse.game.module.shop.message;

import net.minecraft.network.PacketBuffer;

public class SyncUserMessage {

  private final int buyTimeSeconds;
  private final int money;

  public SyncUserMessage(int buyTimeSeconds, int money) {
    this.buyTimeSeconds = buyTimeSeconds;
    this.money = money;
  }

  public int getBuyTimeSeconds() {
    return this.buyTimeSeconds;
  }

  public int getMoney() {
    return this.money;
  }

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.buyTimeSeconds);
    out.writeVarInt(this.money);
  }

  public static SyncUserMessage decode(PacketBuffer in) {
    return new SyncUserMessage(in.readVarInt(), in.readVarInt());
  }
}
