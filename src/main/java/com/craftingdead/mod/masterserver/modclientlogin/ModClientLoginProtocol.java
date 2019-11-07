package com.craftingdead.mod.masterserver.modclientlogin;

import com.craftingdead.mod.masterserver.common.LoginResponsePacket;
import com.craftingdead.mod.masterserver.modclientlogin.packet.EncryptionRequestPacket;
import com.craftingdead.mod.masterserver.modclientlogin.packet.EncryptionResponsePacket;
import com.craftingdead.mod.masterserver.modclientlogin.packet.ModClientLoginPacket;
import com.craftingdead.network.protocol.packet.PacketProtocol;

public class ModClientLoginProtocol extends PacketProtocol<ModClientLoginSession> {

  public static final ModClientLoginProtocol INSTANCE = new ModClientLoginProtocol();

  private ModClientLoginProtocol() {
    this.registerOutbound(ModClientLoginPacket.class, 0x00);
    this.registerOutbound(EncryptionResponsePacket.class, 0x01);
    this.registerInbound(EncryptionRequestPacket.class, 0x00,
        ModClientLoginSession::handleEncryptionRequest);
    this.registerInbound(LoginResponsePacket.class, 0x01,
        ModClientLoginSession::handleLoginResponse);
  }
}
