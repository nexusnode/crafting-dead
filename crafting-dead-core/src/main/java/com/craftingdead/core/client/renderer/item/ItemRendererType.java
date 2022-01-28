package com.craftingdead.core.client.renderer.item;

import java.util.function.BiFunction;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ItemRendererType<I extends Item, P extends ItemRendererProperties>
    extends ForgeRegistryEntry<ItemRendererType<?, ?>> {

  public final static Codec<ItemRendererType<?, ?>> CODEC =
      ResourceLocation.CODEC.xmap(
          registryName -> ItemRendererTypes.REGISTRY.get().getValue(registryName),
          ItemRendererType::getRegistryName);

  private final BiFunction<I, P, CustomItemRenderer> factory;
  private final Class<I> itemType;
  private final Codec<P> propertiesCodec;

  public ItemRendererType(Class<I> itemType, Codec<P> propertiesCodec,
      BiFunction<I, P, CustomItemRenderer> factory) {
    this.itemType = itemType;
    this.propertiesCodec = propertiesCodec;
    this.factory = factory;
  }

  public CustomItemRenderer create(I item, P properties) {
    return this.factory.apply(item, properties);
  }

  public Class<I> getItemType() {
    return this.itemType;
  }

  public Codec<? extends ItemRendererProperties> getPropertiesCodec() {
    return this.propertiesCodec;
  }
}
