package com.craftingdead.immerse.world.level.extension;

import java.util.UUID;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

/**
 * An object which is capable of owning/claiming land.
 * 
 * @author Sm0keySa1m0n
 */
public interface LandOwner {

  Codec<LandOwner> CODEC =
      LandOwnerType.CODEC.dispatch(LandOwner::getType, LandOwnerType::getCodec);

  Codec<LandOwner> NETWORK_CODEC =
      LandOwnerType.CODEC.dispatch(LandOwner::getType, LandOwnerType::getNetworkCodec);

  default boolean isAllowedToBuild(Entity entity, BlockPos blockPos) {
    return this.isAllowedToBuild(entity.getUUID(), blockPos);
  }

  boolean isAllowedToBuild(UUID playerId, BlockPos blockPos);

  CompoundTag getUpdateTag();

  void handleUpdateTag(CompoundTag tag);

  UUID getId();

  LandOwnerType getType();
}
