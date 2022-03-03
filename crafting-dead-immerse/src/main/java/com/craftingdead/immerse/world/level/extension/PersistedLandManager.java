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
import java.nio.file.Path;
import java.util.SortedSet;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
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

  private static final Logger logger = LogUtils.getLogger();

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
