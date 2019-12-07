package com.craftingdead.mod.client.model.builtin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class ItemRendererDispatcher extends ItemStackTileEntityRenderer {

  public static final ItemRendererDispatcher instance = new ItemRendererDispatcher();

  @Override
  public void renderByItem(ItemStack itemStack) {
    IBakedModel model = Minecraft.getInstance().getItemRenderer().getModelWithOverrides(itemStack);
    if (model instanceof BakedBuiltinModel) {
      ((BakedBuiltinModel) model).getItemRenderer().renderItem(itemStack);
    }
  }
}
