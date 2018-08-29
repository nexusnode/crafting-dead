package com.recastproductions.network.packet;

import io.netty.buffer.ByteBuf;

public interface IPacket {

    /**
     * Serialise the {@link IPacket} into a {@link ByteBuf}
     *
     * @param out - the {@link ByteBuf} to write to
     */
    void toBytes(ByteBuf out) throws Exception;

    /**
     * Deserialise the {@link IPacket} from a {@link ByteBuf}
     *
     * @param in
     */
    void fromBytes(ByteBuf in) throws Exception;

}
