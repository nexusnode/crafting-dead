package com.craftingdead.immerse.world.level.extension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class LandOwnerType extends ForgeRegistryEntry<LandOwnerType> {

  public static final Codec<LandOwnerType> CODEC =
      ResourceLocation.CODEC.flatXmap(registryName -> {
        var type = LandOwnerTypes.registry.get().getValue(registryName);
        return type == null
            ? DataResult.error("Unknown registry key: " + registryName.toString())
            : DataResult.success(type);
      }, type -> DataResult.success(type.getRegistryName()));

  private final Codec<? extends LandOwner> codec;
  private final Codec<? extends LandOwner> networkCodec;

  public LandOwnerType(Codec<? extends LandOwner> codec, Codec<? extends LandOwner> networkCodec) {
    this.codec = codec;
    this.networkCodec = networkCodec;
  }

  public Codec<? extends LandOwner> getCodec() {
    return this.codec;
  }

  public Codec<? extends LandOwner> getNetworkCodec() {
    return this.networkCodec;
  }
}
