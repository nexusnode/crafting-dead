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

package com.craftingdead.core.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.IItemHandler;

public class CollectMagazineItemHandlers extends Event {

  private final LivingExtension<?, ?> living;

  private final List<IItemHandler> itemHandlers = new ArrayList<>();

  public CollectMagazineItemHandlers(LivingExtension<?, ?> living) {
    this.living = living;
  }

  public LivingExtension<?, ?> getLiving() {
    return living;
  }

  public void addItemHandler(IItemHandler itemHandler) {
    this.itemHandlers.add(itemHandler);
  }

  public Collection<IItemHandler> getItemHandlers() {
    return Collections.unmodifiableCollection(this.itemHandlers);
  }
}
