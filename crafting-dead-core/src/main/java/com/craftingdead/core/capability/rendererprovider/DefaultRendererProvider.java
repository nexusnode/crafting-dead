package com.craftingdead.core.capability.rendererprovider;

import java.util.function.Supplier;
import com.craftingdead.core.client.renderer.item.IItemRenderer;

public class DefaultRendererProvider implements IRendererProvider {

  private final Supplier<IItemRenderer> itemRendererFactory;

  private IItemRenderer itemRenderer;

  public DefaultRendererProvider() {
    this(() -> null);
  }

  public DefaultRendererProvider(Supplier<IItemRenderer> itemRendererFactory) {
    this.itemRendererFactory = itemRendererFactory;
  }

  @Override
  public IItemRenderer getItemRenderer() {
    return this.itemRenderer == null ? this.itemRenderer = this.itemRendererFactory.get()
        : this.itemRenderer;
  }
}
