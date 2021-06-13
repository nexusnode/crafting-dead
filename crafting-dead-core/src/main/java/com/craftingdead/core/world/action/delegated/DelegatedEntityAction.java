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

package com.craftingdead.core.world.action.delegated;

import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.util.RayTraceUtil;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.EntityRayTraceResult;

public final class DelegatedEntityAction
    extends AbstractDelegatedAction<DelegatedEntityActionType> {

  private final LivingExtension<?, ?> selectedTarget;

  DelegatedEntityAction(DelegatedEntityActionType type, LivingExtension<?, ?> selectedTarget) {
    super(type);
    this.selectedTarget = selectedTarget;
  }

  @Override
  public boolean canPerform(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target,
      ItemStack heldStack) {
    if (!this.type.getCondition().getAsBoolean()) {
      return false;
    }

    if (!performer.getLevel().isClientSide()) {
      Optional<EntityRayTraceResult> entityRayTraceResult =
          RayTraceUtil.rayTraceEntities(performer.getEntity());
      if (this.selectedTarget != performer
          && this.selectedTarget.getEntity() != entityRayTraceResult
              .map(EntityRayTraceResult::getEntity)
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
        && performer.getEntity().getRandom().nextFloat() < this.type.getCustomAction().getRight()) {
      this.type.getCustomAction().getLeft().accept(this.selectedTarget);
    }

    this.selectedTarget.getEntity().curePotionEffects(heldStack);

    for (Pair<Supplier<EffectInstance>, Float> pair : this.type.getEffects()) {
      if (pair.getLeft() != null
          && performer.getEntity().getRandom().nextFloat() < pair.getRight()) {
        EffectInstance effectInstance = pair.getLeft().get();
        if (effectInstance.getEffect().isInstantenous()) {
          effectInstance.getEffect().applyInstantenousEffect(this.selectedTarget.getEntity(),
              this.selectedTarget.getEntity(),
              this.selectedTarget.getEntity(), effectInstance.getAmplifier(), 1.0D);
        } else {
          this.selectedTarget.getEntity().addEffect(new EffectInstance(effectInstance));
        }
      }
    }

    return true;
  }
}
