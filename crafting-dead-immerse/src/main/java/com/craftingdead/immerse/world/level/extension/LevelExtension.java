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

import java.io.IOException;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

public sealed interface LevelExtension
    extends AutoCloseable, INBTSerializable<CompoundTag>permits LevelExtensionImpl {

  Capability<LevelExtension> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  /**
   * @see {@link net.minecraftforge.event.AttachCapabilitiesEvent}
   */
  ResourceLocation CAPABILITY_KEY = new ResourceLocation(CraftingDeadImmerse.ID, "level_extension");

  static LevelExtension create(Level level) {
    return new LevelExtensionImpl(level);
  }

  static LevelExtension getOrThrow(Level level) {
    return CapabilityUtil.getOrThrow(CAPABILITY, level, LevelExtension.class);
  }

  @Nullable
  static LevelExtension get(Level level) {
    return CapabilityUtil.get(CAPABILITY, level, LevelExtension.class);
  }

  /**
   * Tick level logic, only called on the server.
   * 
   * @param haveTime - whether the server has enough time to perform any additional tasks
   */
  void tick(BooleanSupplier haveTime);

  LandManager getLandManager();

  @Override
  void close() throws IOException;
}
