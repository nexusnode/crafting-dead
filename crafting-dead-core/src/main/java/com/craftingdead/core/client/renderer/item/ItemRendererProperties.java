package com.craftingdead.core.client.renderer.item;

import com.mojang.serialization.Codec;

public interface ItemRendererProperties {

  Codec<? extends ItemRendererProperties> CODEC =
      ItemRendererType.CODEC.dispatch(ItemRendererProperties::getItemRendererType,
          ItemRendererType::getPropertiesCodec);

  ItemRendererType<?, ?> getItemRendererType();
}
