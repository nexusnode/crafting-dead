/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.decoration.client;

import java.util.function.Supplier;
import com.craftingdead.decoration.world.level.block.DecorationBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

class RenderLayers {

  static void register() {
    cutout(DecorationBlocks.AA_POSTER);

    // cutout(DecorationBlocks.WOODEN_PALLET);
    // cutout(DecorationBlocks.STACKED_WOODEN_PALLETS);
    cutout(DecorationBlocks.CRATE_ON_WOODEN_PALLET);
    // cutout(DecorationBlocks.CLOTHING_RACK);
    // cutout(DecorationBlocks.BATTEN_LIGHT);
    // cutout(DecorationBlocks.LIT_BATTEN_LIGHT);

    // cutout(DecorationBlocks.OAK_PLANK_BARRICADE_1);
    // cutout(DecorationBlocks.OAK_PLANK_BARRICADE_2);
    // cutout(DecorationBlocks.OAK_PLANK_BARRICADE_3);
    // cutout(DecorationBlocks.SPRUCE_PLANK_BARRICADE_1);
    // cutout(DecorationBlocks.SPRUCE_PLANK_BARRICADE_2);
    // cutout(DecorationBlocks.SPRUCE_PLANK_BARRICADE_3);
    // cutout(DecorationBlocks.BIRCH_PLANK_BARRICADE_1);
    // cutout(DecorationBlocks.BIRCH_PLANK_BARRICADE_2);
    // cutout(DecorationBlocks.BIRCH_PLANK_BARRICADE_3);
    // cutout(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_1);
    // cutout(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_2);
    // cutout(DecorationBlocks.DARK_OAK_PLANK_BARRICADE_3);

    cutout(DecorationBlocks.COMPUTER_1);
    cutout(DecorationBlocks.COMPUTER_2);
    cutout(DecorationBlocks.COMPUTER_3);
    cutout(DecorationBlocks.BROKEN_COMPUTER);

    cutout(DecorationBlocks.LAPTOP_1);
    cutout(DecorationBlocks.LAPTOP_2);
    cutout(DecorationBlocks.LAPTOP_3);
    cutout(DecorationBlocks.BROKEN_LAPTOP);

    cutout(DecorationBlocks.TOILET);
    cutout(DecorationBlocks.DIRTY_TOILET);

    cutout(DecorationBlocks.TOOL_2);
    cutout(DecorationBlocks.TOOL_3);

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
    cutout(DecorationBlocks.LIGHT_SWITCH);
    cutout(DecorationBlocks.ELECTRICAL_SOCKET);
    cutout(DecorationBlocks.ABANDONED_CAMPFIRE);
    
    translucent(DecorationBlocks.QUARTZ_GLASS);
    translucent(DecorationBlocks.VIBRANT_QUARTZ_GLASS);
    
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_BLACK);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_BLUE);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_DARKGREEN);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_ORANGE);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_PINK);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_PURPLE);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_RED);
    translucent(DecorationBlocks.SLEEPING_BAG_CLOSED_YELLOW);
    
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_BLACK);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_BLUE);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_DARKGREEN);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_ORANGE);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_PINK);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_PURPLE);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_RED);
    translucent(DecorationBlocks.SLEEPING_BAG_OPEN_YELLOW);
    
    cutout(DecorationBlocks.BOXES_OF_BULLETS);
    
    translucent(DecorationBlocks.FENCE);
    translucent(DecorationBlocks.SEWER_OPENING);
    translucent(DecorationBlocks.HL2_GLASS);
    translucent(DecorationBlocks.HL2_TRASH);
  }

  private static void cutout(Supplier<? extends Block> block) {
    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
  }

  private static void translucent(Supplier<? extends Block> block) {
    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent());
  }
}
