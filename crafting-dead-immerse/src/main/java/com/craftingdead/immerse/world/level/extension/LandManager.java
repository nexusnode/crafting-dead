/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

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

  void tick(BooleanSupplier haveTime);

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
  CompletionStage<ClaimResult> registerLandClaim(LandClaim landClaim);

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
    return this.getLandClaimAt(blockPos) != null;
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

  enum ClaimResult {

    OUT_OF_BOUNDS,
    ALREADY_CLAIMED,
    SUCCESS;
  }
}
