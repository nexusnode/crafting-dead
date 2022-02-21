package com.craftingdead.immerse.world.level.extension;

import java.io.IOException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

final class LevelExtensionImpl implements LevelExtension {

  private final Level level;
  private final LandManager landManager;

  LevelExtensionImpl(Level level) {
    this.level = level;
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
  public void tick() {
    if (!this.level.isClientSide()) {
      var server = this.level.getServer();
      this.landManager.serverTick(server::haveTime);
    }
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
