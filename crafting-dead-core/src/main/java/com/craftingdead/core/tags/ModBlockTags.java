package com.craftingdead.core.tags;

import com.craftingdead.core.CraftingDead;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {

  public static final TagKey<Block> BULLETS_PASS_THROUGH = bind("bullets_pass_through");

  private static TagKey<Block> bind(String name) {
    return BlockTags.create(new ResourceLocation(CraftingDead.ID, name));
  }
}
