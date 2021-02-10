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

package com.craftingdead.core.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {

  public static final ItemGroup CRAFTING_DEAD_MED = (new ItemGroup("craftingdead.med") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.FIRST_AID_KIT::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_CLOTHING = (new ItemGroup("craftingdead.clothing") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.BUILDER_CLOTHING::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_COMBAT = (new ItemGroup("craftingdead.combat") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.AK47::get);
    }
  });

  public static final ItemGroup CRAFTING_DEAD_MISC = (new ItemGroup("craftingdead.misc") {

    @Override
    public ItemStack createIcon() {
      return new ItemStack(ModItems.MEDICAL_DROP_RADIO::get);
    }
  });
}
