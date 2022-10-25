package com.craftingdead.decoration.client;

import java.util.function.Supplier;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

class RenderLayers {

  static void register() {
    cutout(DecorationBlocks.WOODEN_PALLET);
    cutout(DecorationBlocks.STACKED_WOODEN_PALLETS);
    cutout(DecorationBlocks.CRATE_ON_WOODEN_PALLET);
  }

  private static void cutout(Supplier<? extends Block> block) {
    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
  }
}
