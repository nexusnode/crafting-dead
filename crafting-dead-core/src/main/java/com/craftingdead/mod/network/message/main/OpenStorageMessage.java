package com.craftingdead.mod.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.living.player.ServerPlayer;
import com.craftingdead.mod.inventory.InventorySlotType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class OpenStorageMessage {

  private final InventorySlotType slotType;

  public OpenStorageMessage(InventorySlotType slotType) {
    this.slotType = slotType;
  }

  public static void encode(OpenStorageMessage msg, PacketBuffer out) {
    out.writeEnumValue(msg.slotType);
  }

  public static OpenStorageMessage decode(PacketBuffer in) {
    return new OpenStorageMessage(in.readEnumValue(InventorySlotType.class));
  }

  public static boolean handle(OpenStorageMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx
        .get()
        .getSender()
        .getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof ServerPlayer)
        .map(living -> (ServerPlayer) living)
        .ifPresent(player -> player.openStorage(msg.slotType));
    return true;
  }
}
