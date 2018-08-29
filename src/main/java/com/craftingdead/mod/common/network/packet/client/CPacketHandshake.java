package com.craftingdead.mod.common.network.packet.client;

import java.io.IOException;

import com.craftingdead.mod.common.network.CraftingDeadContext;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;
import com.recastproductions.network.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;

public class CPacketHandshake implements IPacket {

    private String[] mods;

    public CPacketHandshake() {
        ;
    }

    public CPacketHandshake(String[] mods) {
        this.mods = mods;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            mods = ByteBufUtils.readStringArray(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            ByteBufUtils.writeStringArray(buf, mods);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getMods() {
        return this.mods;
    }

    public static class CPacketHandlerHandshake
            implements IPacketHandler<CPacketHandshake, IPacket, CraftingDeadContext> {

        @Override
        public IPacket processPacket(CPacketHandshake packet, CraftingDeadContext ctx) {
            ctx.getModClient().getLogicalServer().onHandshake(ctx.getServerHandler().player, packet);
            return null;
        }

    }

}
