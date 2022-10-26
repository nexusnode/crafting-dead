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
    translucent(DecorationBlocks.WASHING_MACHINE);
    translucent(DecorationBlocks.BROKEN_WASHING_MACHINE);
    cutout(DecorationBlocks.POLE_BARRIER);
    cutout(DecorationBlocks.STREET_LIGHT_HEAD);
    cutout(DecorationBlocks.CHERRY_LEAVES);
    cutout(DecorationBlocks.GOLD_CHAIN);
    cutout(DecorationBlocks.BASALT_LANTERN);
    cutout(DecorationBlocks.COUNTER);
    cutout(DecorationBlocks.COUNTER_CORNER);
    cutout(DecorationBlocks.COUNTER_SINK);
    cutout(DecorationBlocks.SINK);
    cutout(DecorationBlocks.PLATE);
    cutout(DecorationBlocks.WHITE_CHAIR);
    cutout(DecorationBlocks.BLACK_CHAIR);
    cutout(DecorationBlocks.RED_CHAIR);
    cutout(DecorationBlocks.BLUE_CHAIR);
    cutout(DecorationBlocks.GREEN_CHAIR);
    cutout(DecorationBlocks.ORANGE_CHAIR);
    cutout(DecorationBlocks.MAGENTA_CHAIR);
    cutout(DecorationBlocks.LIGHT_BLUE_CHAIR);
    cutout(DecorationBlocks.YELLOW_CHAIR);
    cutout(DecorationBlocks.LIME_CHAIR);
    cutout(DecorationBlocks.PINK_CHAIR);
    cutout(DecorationBlocks.GRAY_CHAIR);
    cutout(DecorationBlocks.LIGHT_GRAY_CHAIR);
    cutout(DecorationBlocks.CYAN_CHAIR);
    cutout(DecorationBlocks.PURPLE_CHAIR);
    cutout(DecorationBlocks.BROWN_CHAIR);
  }

  private static void cutout(Supplier<? extends Block> block) {
    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
  }

  private static void translucent(Supplier<? extends Block> block) {
    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent());
  }
}
