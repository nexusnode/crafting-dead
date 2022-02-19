package com.craftingdead.immerse.world.level.extension;

import java.io.IOException;
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
    extends AutoCloseable, INBTSerializable<CompoundTag> permits LevelExtensionImpl {

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

  void tick();

  LandManager getLandManager();

  @Override
  void close() throws IOException;
}
