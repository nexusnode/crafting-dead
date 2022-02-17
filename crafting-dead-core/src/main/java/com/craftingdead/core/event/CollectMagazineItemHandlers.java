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
    return this.living;
  }

  public void addItemHandler(IItemHandler itemHandler) {
    this.itemHandlers.add(itemHandler);
  }

  public Collection<IItemHandler> getItemHandlers() {
    return Collections.unmodifiableCollection(this.itemHandlers);
  }
}
