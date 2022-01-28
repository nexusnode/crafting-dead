package com.craftingdead.core.client.renderer.item;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.item.gun.GunItem;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class ItemRendererTypes {

  @SuppressWarnings("unchecked")
  public static final DeferredRegister<ItemRendererType<?, ?>> ITEM_RENDERER_TYPES =
      DeferredRegister.create((Class<ItemRendererType<?, ?>>) (Class<?>) ItemRendererType.class,
          CraftingDead.ID);

  public static final Lazy<IForgeRegistry<ItemRendererType<?, ?>>> REGISTRY =
      Lazy.of(ITEM_RENDERER_TYPES.makeRegistry("item_renderer_type", RegistryBuilder::new));

  public static final RegistryObject<ItemRendererType<?, ?>> GUN =
      ITEM_RENDERER_TYPES.register("gun",
          () -> new ItemRendererType<>(GunItem.class, GunRendererProperties.CODEC,
              GunRenderer::new));
}
