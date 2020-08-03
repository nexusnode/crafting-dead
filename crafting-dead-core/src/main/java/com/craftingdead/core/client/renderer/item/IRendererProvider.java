package com.craftingdead.core.client.renderer.item;

import net.minecraft.item.Item;

/**
 * An interface that allows an {@link Item} to provide an {@link IItemRenderer}.
 */
public interface IRendererProvider {

  /**
   * A singleton instance of the {@link IItemRenderer} to use.
   * 
   * @return the {@link IItemRenderer} instance
   */
  IItemRenderer getRenderer();
}
