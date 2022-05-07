/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.world.level.extension;

import com.craftingdead.core.world.entity.extension.LivingExtension;
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

  void playerPlacedBlock(LivingExtension<?, ?> player, BlockPos... blocks);

  void playerRemovedBlock(LivingExtension<?, ?> player, BlockPos... blocks);

  CompoundTag getUpdateTag();

  void handleUpdateTag(CompoundTag tag);

  UUID getId();

  LandOwnerType getType();
}
