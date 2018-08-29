package com.craftingdead.network.packet.handshake.client;

import com.recastproductions.network.packet.IHandshakePacket;
import com.recastproductions.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class CPacketHandshakeModClient implements IHandshakePacket {

    private String accessToken;
    private String clientToken;
    private String username;
    private UUID playerUUID = UUID.randomUUID();
    private String modVersion;

    public CPacketHandshakeModClient() {
        ;
    }

    public CPacketHandshakeModClient(String accessToken, String clientToken, String username, UUID playerUUID,
                                     String modVersion) {
        this.accessToken = accessToken;
        this.clientToken = clientToken;
        this.username = username;
        this.playerUUID = playerUUID;
        this.modVersion = modVersion;
    }

    @Override
    public void fromBytes(ByteBuf buf) throws Exception {
        if (buf.readBoolean())
            playerUUID = ByteBufUtils.readUUID(buf);
        accessToken = ByteBufUtils.readUTF8(buf);
        clientToken = ByteBufUtils.readUTF8(buf);
        username = ByteBufUtils.readUTF8(buf);
        modVersion = ByteBufUtils.readUTF8(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) throws Exception {
        buf.writeBoolean(playerUUID != null);
        if (playerUUID != null)
            ByteBufUtils.writeUUID(buf, playerUUID);
        ByteBufUtils.writeUTF8(buf, accessToken);
        ByteBufUtils.writeUTF8(buf, clientToken);
        ByteBufUtils.writeUTF8(buf, username);
        ByteBufUtils.writeUTF8(buf, modVersion);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getUsername() {
        return this.username;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getModVersion() {
        return modVersion;
    }

}
