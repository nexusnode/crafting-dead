package gg.chaldea.client.reset.packet.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.HandshakeMessages;

public class S2CReset extends HandshakeMessages.LoginIndexedMessage {

  public void encode(FriendlyByteBuf buffer) {}

  public static S2CReset decode(FriendlyByteBuf buffer) {
    return new S2CReset();
  }
}
