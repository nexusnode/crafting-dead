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

package com.craftingdead.core.world.action.delegate;

import java.util.Optional;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.sounds.SoundEvent;

public abstract class AbstractDelegateAction<T extends AbstractDelegateActionType>
    implements DelegateAction {

  protected final T type;

  public AbstractDelegateAction(T type) {
    this.type = type;
  }

  @Override
  public boolean shouldConsumeItem(LivingExtension<?, ?> performer) {
    return this.type.shouldConsumeItemInCreative()
        && !(performer.getEntity() instanceof Player player && player.isCreative())
        && this.type.shouldConsumeItem();
  }

  @Override
  public Optional<Item> getResultItem(LivingExtension<?, ?> performer) {
    return (!this.type.useResultItemInCreative()
        && performer.getEntity() instanceof Player player
        && player.isCreative()) ? Optional.empty() : this.type.getReturnItem();
  }

  @Override
  public Optional<SoundEvent> getFinishSound() {
    return this.type.getFinishSound();
  }
}
