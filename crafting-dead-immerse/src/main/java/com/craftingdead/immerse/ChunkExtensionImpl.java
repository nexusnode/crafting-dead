package com.craftingdead.immerse;

import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.shorts.Short2LongMap;
import it.unimi.dsi.fastutil.shorts.Short2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.LevelChunk;

public class ChunkExtensionImpl implements ChunkExtension {

  private final LevelChunk chunk;
  private final Short2LongMap[] basesPerSection;

  ChunkExtensionImpl(LevelChunk chunk) {
    this.chunk = chunk;
    this.basesPerSection = new Short2LongMap[chunk.getSectionsCount()];
  }

  @Override
  public boolean registerBase(BlockPos blockPos, BlockPos basePos) {
    var relativePos = SectionPos.sectionRelativePos(blockPos);
    var index = this.chunk.getSectionIndex(blockPos.getY());
    var bases = this.basesPerSection[index];
    if (bases == null) {
      this.basesPerSection[index] = bases = new Short2LongOpenHashMap();
    } else if (bases.containsKey(relativePos)) {
      return false;
    }

    bases.put(relativePos, basePos.asLong());
    return true;
  }

  @Override
  public void removeBase(BlockPos blockPos) {
    var index = this.chunk.getSectionIndex(blockPos.getY());
    var bases = this.basesPerSection[index];
    if (bases != null) {
      bases.remove(SectionPos.sectionRelativePos(blockPos));
      if (bases.isEmpty()) {
        this.basesPerSection[index] = null;
      }
    }
  }

  @Override
  public boolean hasBase(BlockPos blockPos) {
    var bases = this.basesPerSection[this.chunk.getSectionIndex(blockPos.getY())];
    return bases != null && bases.containsKey(SectionPos.sectionRelativePos(blockPos));
  }

  @Nullable
  @Override
  public BlockPos getBase(BlockPos blockPos) {
    var relativePos = SectionPos.sectionRelativePos(blockPos);
    var bases = this.basesPerSection[this.chunk.getSectionIndex(blockPos.getY())];
    if (bases == null || !bases.containsKey(relativePos)) {
      return null;
    }
    return BlockPos.of(bases.get(relativePos));
  }
}
