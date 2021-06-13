/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.survival.data;

import com.craftingdead.core.tags.ModItemTags;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
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
  }

  @Override
  public String getName() {
    return "Crafting Dead Survival Item Tags";
  }
}
