/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.inventory;

import java.util.function.BiPredicate;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PredicateSlot extends Slot {

  private final BiPredicate<PredicateSlot, ItemStack> predicate;

  public PredicateSlot(Container inventory, int index, int xPosition, int yPosition,
      BiPredicate<PredicateSlot, ItemStack> predicate) {
    super(inventory, index, xPosition, yPosition);
    this.predicate = predicate;
  }

  @Override
  public boolean mayPlace(ItemStack itemStack) {
    return this.predicate.test(this, itemStack);
  }
}
