/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.renderer.item;

import java.util.function.BiFunction;
import com.mojang.serialization.Codec;
import net.minecraft.world.item.Item;

public class ItemRendererType<I extends Item, P extends ItemRendererProperties> {

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
