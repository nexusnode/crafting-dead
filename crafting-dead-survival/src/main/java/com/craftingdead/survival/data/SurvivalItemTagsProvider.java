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

package com.craftingdead.survival.data;

import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.tags.SurvivalItemTags;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SurvivalItemTagsProvider extends ItemTagsProvider {

  public SurvivalItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider,
      ExistingFileHelper existingFileHelper) {
    super(dataGenerator, blockTagProvider, CraftingDeadSurvival.ID, existingFileHelper);
  }

  @Override
  public void addTags() {
    this.tag(ModItemTags.SYRINGES).add(SurvivalItems.RBI_SYRINGE.get(),
        SurvivalItems.CURE_SYRINGE.get());

    this.tag(SurvivalItemTags.ZOMBIE_CLOTHING_LOOT).addTag(ModItemTags.CLOTHING);
    this.tag(SurvivalItemTags.ZOMBIE_HAT_LOOT).addTag(ModItemTags.HATS);
    this.tag(SurvivalItemTags.ZOMBIE_HAND_LOOT).addTag(ModItemTags.MELEES);
  }

  @Override
  public String getName() {
    return "Crafting Dead Survival Item Tags";
  }
}
