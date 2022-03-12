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
import java.util.function.BooleanSupplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

final class LevelExtensionImpl implements LevelExtension {

  private final LandManager landManager;

  LevelExtensionImpl(Level level) {
    if (level instanceof ServerLevel) {
      var server = level.getServer();
      var storageSource = server.storageSource;
      var path = storageSource.getDimensionPath(level.dimension());
      this.landManager = new PersistedLandManager(level, path.resolve("land"),
          server.forceSynchronousWrites());
    } else {
      this.landManager = new BaseLandManager(level);
    }
  }

  @Override
  public void tick(BooleanSupplier haveTime) {
    this.landManager.tick(haveTime);
  }

  @Override
  public LandManager getLandManager() {
    return this.landManager;
  }

  @Override
  public void close() throws IOException {
    this.landManager.close();
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    tag.put("landManager", this.landManager.save());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.landManager.load(tag.getCompound("landManager"));
  }
}
