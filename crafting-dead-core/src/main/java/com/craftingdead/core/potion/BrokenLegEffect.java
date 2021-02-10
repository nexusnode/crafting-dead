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

package com.craftingdead.core.potion;

import java.util.ArrayList;
import java.util.List;
import com.craftingdead.core.item.ModItems;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BrokenLegEffect extends Effect {

  public BrokenLegEffect() {
    super(EffectType.HARMFUL, 0x816C5A);
    this.addAttributesModifier(Attributes.MOVEMENT_SPEED, "021BEAA1-498F-4D7B-933E-F0FA0B88B9D1",
        -0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
  }

  @Override
  public boolean isReady(int duration, int amplifier) {
    return true;
  }

  @Override
  public List<ItemStack> getCurativeItems() {
    List<ItemStack> items = new ArrayList<ItemStack>();
    items.add(new ItemStack(ModItems.FIRST_AID_KIT::get));
    items.add(new ItemStack(ModItems.SPLINT::get));
    return items;
  }
}
