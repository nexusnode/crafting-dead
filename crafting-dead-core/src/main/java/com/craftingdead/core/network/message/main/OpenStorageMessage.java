package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.capability.living.Player;
import com.craftingdead.core.inventory.InventorySlotType;
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
    Player.get(ctx.get().getSender()).openStorage(msg.slotType);
    return true;
  }
}
