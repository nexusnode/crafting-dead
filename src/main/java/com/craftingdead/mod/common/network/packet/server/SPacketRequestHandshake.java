package com.craftingdead.mod.common.network.packet.server;

import com.craftingdead.mod.common.network.CraftingDeadContext;
import com.craftingdead.mod.common.network.packet.client.CPacketHandshake;
import com.recastproductions.network.packet.IPacket;
import com.recastproductions.network.packet.IPacketHandler;

import io.netty.buffer.ByteBuf;

public class SPacketRequestHandshake implements IPacket {

    @Override
    public void fromBytes(ByteBuf buf) {
        ;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ;
    }

    public static class SPacketHandlerRequestHandshake
            implements IPacketHandler<SPacketRequestHandshake, CPacketHandshake, CraftingDeadContext> {

        @Override
        public CPacketHandshake processPacket(SPacketRequestHandshake packet, CraftingDeadContext ctx) {
            return ctx.getModClient().getHandshakePacket();
        }

    }

}
