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

import javax.annotation.Nullable;
import com.craftingdead.core.util.RayTraceUtil;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public final class DelegateEntityAction<T extends LivingExtension<?, ?>>
    extends AbstractDelegateAction<DelegateEntityActionType<T>> {

  private final T selectedTarget;

  DelegateEntityAction(DelegateEntityActionType<T> type, T selectedTarget) {
    super(type);
    this.selectedTarget = selectedTarget;
  }

  @Override
  public boolean canPerform(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target,
      ItemStack heldStack) {
    if (!performer.getLevel().isClientSide()) {
      var entityRayTraceResult = RayTraceUtil.rayTraceEntities(performer.getEntity());
      if (this.selectedTarget != performer
          && this.selectedTarget.getEntity() != entityRayTraceResult
              .map(EntityHitResult::getEntity)
              .orElse(null)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean finish(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target,
      ItemStack heldStack) {
    if (this.type.getCustomAction() != null
        && performer.getEntity().getRandom().nextFloat() < this.type.getCustomAction().chance()) {
      this.type.getCustomAction().consumer().accept(performer, this.selectedTarget);
    }

    this.selectedTarget.getEntity().curePotionEffects(heldStack);

    for (var action : this.type.getEffects()) {
      if (performer.getEntity().getRandom().nextFloat() < action.chance()) {
        var effectInstance = action.effect().get();
        if (effectInstance.getEffect().isInstantenous()) {
          effectInstance.getEffect().applyInstantenousEffect(this.selectedTarget.getEntity(),
              this.selectedTarget.getEntity(),
              this.selectedTarget.getEntity(), effectInstance.getAmplifier(), 1.0D);
        } else {
          this.selectedTarget.getEntity().addEffect(new MobEffectInstance(effectInstance));
        }
      }
    }

    return true;
  }
}
