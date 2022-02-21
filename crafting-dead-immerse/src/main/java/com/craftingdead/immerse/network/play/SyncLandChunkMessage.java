package com.craftingdead.immerse.network.play;

import java.util.function.Supplier;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

public record SyncLandChunkMessage(ChunkPos chunkPos, FriendlyByteBuf buf) {

  public void encode(FriendlyByteBuf out) {
    out.writeChunkPos(this.chunkPos);
    out.writeVarInt(this.buf.readableBytes());
    out.writeBytes(this.buf);
    this.buf.release();
  }

  public static SyncLandChunkMessage decode(FriendlyByteBuf in) {
    return new SyncLandChunkMessage(in.readChunkPos(),
        new FriendlyByteBuf(in.readBytes(in.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    context.get()
        .enqueueWork(() -> LogicalSidedProvider.CLIENTWORLD
            .get(context.get().getDirection().getReceptionSide())
            .map(LevelExtension::getOrThrow)
            .map(LevelExtension::getLandManager)
            .ifPresent(landManager -> landManager.readChunkFromBuf(this.chunkPos, this.buf)))
        .thenRun(this.buf::release);
    return true;
  }
}
