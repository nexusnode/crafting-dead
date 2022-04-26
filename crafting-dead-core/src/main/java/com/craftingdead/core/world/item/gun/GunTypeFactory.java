package com.craftingdead.core.world.item.gun;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class GunTypeFactory extends ForgeRegistryEntry<GunTypeFactory> {

  public static final Codec<GunTypeFactory> CODEC =
      ResourceLocation.CODEC.flatXmap(registryName -> {
        var gunType = GunTypeFactories.registry.get().getValue(registryName);
        return gunType == null
            ? DataResult.error("Unknown registry key: " + registryName.toString())
            : DataResult.success(gunType);
      }, gameType -> DataResult.success(gameType.getRegistryName()));

  private final Codec<? extends GunType> gunTypeCodec;

  public GunTypeFactory(Codec<? extends GunType> gunTypeCodec) {
    this.gunTypeCodec = gunTypeCodec;
  }

  public Codec<? extends GunType> getGunTypeCodec() {
    return this.gunTypeCodec;
  }
}
