package com.craftingdead.core.network.message.main;

import java.util.function.Supplier;
import com.craftingdead.core.client.gui.SelectTeamScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SelectTeamMessage {

  public static void encode(SelectTeamMessage msg, PacketBuffer out) {}

  public static SelectTeamMessage decode(PacketBuffer in) {
    return new SelectTeamMessage();
  }

  public static boolean handle(SelectTeamMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get()
        .enqueueWork(() -> Minecraft.getInstance().displayGuiScreen(new SelectTeamScreen(false)));
    return true;
  }
}
