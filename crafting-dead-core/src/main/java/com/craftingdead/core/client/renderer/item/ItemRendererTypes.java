package com.craftingdead.core.client.renderer.item;

import java.util.Optional;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.item.gun.GunItem;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;

public class ItemRendererTypes {

  public final static Codec<ItemRendererType<?, ?>> CODEC =
      ResourceLocation.CODEC.flatXmap(ItemRendererTypes::getValueDataResult,
          ItemRendererTypes::getKeyDataResult);

  private static final BiMap<ResourceLocation, ItemRendererType<?, ?>> registry =
      HashBiMap.create();

  public static final ItemRendererType<GunItem, GunRendererProperties> GUN = register("gun",
      new ItemRendererType<>(GunItem.class, GunRendererProperties.CODEC, GunRenderer::new));

  public static <T extends ItemRendererType<?, ?>> T register(String id, T itemRendererType) {
    return register(new ResourceLocation(CraftingDead.ID, id), itemRendererType);
  }

  public static <T extends ItemRendererType<?, ?>> T register(ResourceLocation id,
      T itemRendererType) {
    if (registry.put(id, itemRendererType) != null) {
      throw new IllegalStateException("Duplicate key: " + id.toString());
    }
    return itemRendererType;
  }

  public static Optional<ItemRendererType<?, ?>> getValue(ResourceLocation key) {
    return Optional.ofNullable(registry.get(key));
  }

  private static DataResult<? extends ItemRendererType<?, ?>> getValueDataResult(
      ResourceLocation key) {
    return getValue(key)
        .map(DataResult::success)
        .orElseGet(() -> DataResult.error("Key not found: " + key.toString()));
  }

  public static Optional<ResourceLocation> getKey(ItemRendererType<?, ?> value) {
    return Optional.ofNullable(registry.inverse().get(value));
  }

  private static DataResult<? extends ResourceLocation> getKeyDataResult(
      ItemRendererType<?, ?> value) {
    return getKey(value)
        .map(DataResult::success)
        .orElseGet(() -> DataResult.error("Value not found: " + value.getClass().getName()));
  }
}
