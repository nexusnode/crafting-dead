package com.craftingdead.immerse;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public interface ChunkExtension {

  Capability<ChunkExtension> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  static ChunkExtension create(LevelChunk chunk) {
    return new ChunkExtensionImpl(chunk);
  }

  static ChunkExtension getOrThrow(Level level, BlockPos blockPos) {
    return getOrThrow(level.getChunkAt(blockPos));
  }

  static ChunkExtension getOrThrow(LevelChunk chunk) {
    return chunk.getCapability(CAPABILITY).orElseThrow(
        () -> new IllegalStateException("Expected ChunkExtension at " + chunk.getPos()));
  }

  boolean registerBase(BlockPos blockPos, BlockPos basePos);

  void removeBase(BlockPos blockPos);

  boolean hasBase(BlockPos blockPos);

  @Nullable
  BlockPos getBase(BlockPos blockPos);
}
