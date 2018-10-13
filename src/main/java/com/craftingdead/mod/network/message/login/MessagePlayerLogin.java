package com.craftingdead.mod.network.message.login;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import sm0keysa1m0n.network.message.Message;
import sm0keysa1m0n.network.util.ByteBufUtil;

public class MessagePlayerLogin implements Message {

    private String accessToken;
    private String clientToken;
    private String username;
    private UUID playerUUID = UUID.randomUUID();
    private String modVersion;

    public MessagePlayerLogin() {
        ;
    }

    public MessagePlayerLogin(String accessToken, String clientToken, String username, UUID playerUUID,
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
            playerUUID = ByteBufUtil.readUUID(buf);
        accessToken = ByteBufUtil.readUTF8(buf);
        clientToken = ByteBufUtil.readUTF8(buf);
        username = ByteBufUtil.readUTF8(buf);
        modVersion = ByteBufUtil.readUTF8(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) throws Exception {
        buf.writeBoolean(playerUUID != null);
        if (playerUUID != null)
            ByteBufUtil.writeUUID(buf, playerUUID);
        ByteBufUtil.writeUTF8(buf, accessToken);
        ByteBufUtil.writeUTF8(buf, clientToken);
        ByteBufUtil.writeUTF8(buf, username);
        ByteBufUtil.writeUTF8(buf, modVersion);
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
