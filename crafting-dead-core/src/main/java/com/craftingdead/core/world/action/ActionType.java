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

package com.craftingdead.core.world.action;

import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ActionType extends ForgeRegistryEntry<ActionType> {

  private final boolean triggeredByClient;
  private final Factory<ActionType> factory;

  @SuppressWarnings("unchecked")
  public <SELF extends ActionType> ActionType(boolean triggeredByClient,
      Factory<? super SELF> factory) {
    this.triggeredByClient = triggeredByClient;
    this.factory = (Factory<ActionType>) factory;
  }

  public Action createAction(LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target) {
    return this.factory.create(this, performer, target);
  }

  public boolean isTriggeredByClient() {
    return this.triggeredByClient;
  }

  public interface Factory<T extends ActionType> {
    Action create(T actionType, LivingExtension<?, ?> performer,
        @Nullable LivingExtension<?, ?> target);
  }
}
