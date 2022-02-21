package com.craftingdead.immerse.world.level.extension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.SortedSet;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.storage.IOWorker;

public class PersistedLandManager extends BaseLandManager {

  private static final Logger logger = LogManager.getLogger();

  private final IOWorker worker;
  private final SortedSet<ChunkPos> dirtyChunks = new ObjectLinkedOpenHashSet<>();

  public PersistedLandManager(Level level, Path path, boolean sync) {
    super(level);
    this.worker = new IOWorker(path, sync, path.getFileName().toString());
  }

  @Override
  public void serverTick(BooleanSupplier haveTime) {
    super.serverTick(haveTime);
    while (!this.dirtyChunks.isEmpty() && haveTime.getAsBoolean()) {
      this.saveChunk(this.dirtyChunks.first());
    }
  }

  @Nullable
  @Override
  protected LandSection getOrLoadSection(long sectionPos) {
    if (this.isSectionOutOfBounds(sectionPos)) {
      return null;
    }

    if (!this.sections.containsKey(sectionPos)) {
      this.loadChunk(SectionPos.of(sectionPos).chunk());
    }

    return this.sections.get(sectionPos);
  }

  private void loadChunk(ChunkPos chunkPos) {
    CompoundTag chunkTag;
    try {
      chunkTag = this.worker.load(chunkPos);
    } catch (IOException e) {
      logger.error("Error reading chunk {} data from disk", chunkPos, e);
      return;
    }

    for (int i = this.level.getMinSection(); i < this.level.getMaxSection(); ++i) {
      this.sections.put(getSectionPos(chunkPos, i), null);
    }

    if (chunkTag != null) {
      this.loadChunk(chunkPos, chunkTag);
    }
  }

  private void loadChunk(ChunkPos chunkPos, CompoundTag chunkTag) {
    var sectionsTag = chunkTag.getList("sections", Tag.TAG_COMPOUND);
    for (int i = 0; i < sectionsTag.size(); i++) {
      var entryTag = sectionsTag.getCompound(i);
      var sectionY = entryTag.getInt("sectionY");
      if (sectionY < this.level.getMinSection() || sectionY >= this.level.getMaxSection()) {
        continue;
      }
      var sectionKey = getSectionPos(chunkPos, sectionY);
      var section = LandSection.CODEC.parse(NbtOps.INSTANCE, entryTag.getCompound("section"))
          .getOrThrow(false, logger::error);
      this.sections.put(sectionKey, section);
    }
  }

  private void saveChunk(ChunkPos chunkPos) {
    this.dirtyChunks.remove(chunkPos);
    var chunkTag = new CompoundTag();
    var sectionsTag = new ListTag();
    for (int i = this.level.getMinSection(); i < this.level.getMaxSection(); ++i) {
      var key = getSectionPos(chunkPos, i);
      var section = this.sections.get(key);
      if (section != null) {
        var entryTag = new CompoundTag();
        entryTag.putInt("sectionY", i);
        entryTag.put("section", LandSection.CODEC.encodeStart(NbtOps.INSTANCE, section)
            .getOrThrow(false, logger::error));
        sectionsTag.add(entryTag);
      }
    }
    chunkTag.put("sections", sectionsTag);
    this.worker.store(chunkPos, chunkTag);
  }

  @Override
  protected void sectionChanged(long sectionPos) {
    super.sectionChanged(sectionPos);
    var section = this.sections.get(sectionPos);
    if (section != null) {
      this.dirtyChunks.add(SectionPos.of(sectionPos).chunk());
    } else {
      logger.warn("No data for position: {}", SectionPos.of(sectionPos));
    }
  }

  @Override
  public void flush(ChunkPos chunkPos) {
    if (this.dirtyChunks.contains(chunkPos)) {
      this.saveChunk(chunkPos);
    }
  }

  @Override
  public void close() throws IOException {
    super.close();
    this.worker.close();
  }
}
