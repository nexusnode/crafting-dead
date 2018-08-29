package com.recastproductions.network.pipline;

import com.recastproductions.network.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class Varint21FrameEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out)
            throws Exception {
        int i = in.readableBytes();
        int j = ByteBufUtils.getVarIntSize(i);

        if (j > 3) {
            throw new IllegalArgumentException("Unable to fit " + i + " into " + 3);
        } else {
            out.ensureWritable(j + i);
            ByteBufUtils.writeVarInt(out, i);
            out.writeBytes(in, in.readerIndex(), i);
        }
    }
}