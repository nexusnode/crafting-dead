package com.craftingdead.immerse.world.level.extension;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;

public interface LandManager extends AutoCloseable {

  void serverTick(BooleanSupplier haveTime);

  /**
   * Registers a new {@link LandOwner}.
   * 
   * @param landOwner - the land owner to register
   */
  void registerLandOwner(LandOwner landOwner);

  /**
   * <b>Warning: this will remove all claimed land belonging to the specified {@link LandOwner}.</b>
   * 
   * @param landOwner - the land owner to remove
   */
  CompletionStage<Boolean> removeLandOwner(UUID landOwnerId);

  @Nullable
  LandOwner getLandOwner(UUID landOwnerId);

  /**
   * Claims a region of land. <b>Requires the {@link LandOwner} to be registered beforehand via</b>
   * {@link #registerLandOwner(LandOwner)}.
   * 
   * 
   * @param landClaim
   * @return
   */
  CompletionStage<Boolean> registerLandClaim(LandClaim landClaim);

  /**
   * Removes the specified {@link LandClaim}.
   * 
   * @param landClaim - the land claim to remove
   * @return
   */
  CompletionStage<Boolean> removeLandClaim(LandClaim landClaim);

  @Nullable
  LandClaim getLandClaimAt(BlockPos blockPos);

  default boolean isLandClaimed(BlockPos blockPos) {
    return this.getLandClaimAt(blockPos) == null;
  }

  default Optional<LandOwner> getLandOwnerAt(BlockPos blockPos) {
    var landClaim = this.getLandClaimAt(blockPos);
    return landClaim == null ? Optional.empty()
        : Optional.ofNullable(this.getLandOwner(landClaim.ownerId()));
  }

  CompoundTag save();

  void load(CompoundTag tag);

  void writeToBuf(FriendlyByteBuf out);

  void readFromBuf(FriendlyByteBuf in);

  void writeChunkToBuf(ChunkPos chunkPos, FriendlyByteBuf out);

  void readChunkFromBuf(ChunkPos chunkPos, FriendlyByteBuf in);

  void flush(ChunkPos chunkPos);

  @Override
  void close() throws IOException;
}
