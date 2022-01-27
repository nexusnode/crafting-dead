package com.craftingdead.core.client.renderer.item;

import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ItemRendererType extends ForgeRegistryEntry<ItemRendererType> {

  public final static Codec<ItemRendererType> CODEC =
      ResourceLocation.CODEC.xmap(
          registryName -> ItemRendererTypes.REGISTRY.get().getValue(registryName),
          ItemRendererType::getRegistryName);

  private final Codec<? extends CustomItemRenderer> itemRendererCodec;

  public ItemRendererType(Codec<? extends CustomItemRenderer> itemRendererCodec) {
    this.itemRendererCodec = itemRendererCodec;
  }

  public Codec<? extends CustomItemRenderer> getCodec() {
    return this.itemRendererCodec;
  }
}
