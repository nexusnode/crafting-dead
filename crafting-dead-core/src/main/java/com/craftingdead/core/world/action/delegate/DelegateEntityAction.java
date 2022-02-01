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
