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

package com.craftingdead.core.world.action.item;

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.util.RayTraceUtil;
import com.craftingdead.core.world.action.ActionObserver;
import com.craftingdead.core.world.action.ProgressBar;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;

public final class EntityItemAction<T extends LivingExtension<?, ?>> extends ItemAction {

  private final EntityItemActionType<T> type;
  private final LivingExtension<?, ?> performer;

  @Nullable
  private final T selectedTarget;

  protected EntityItemAction(InteractionHand hand, EntityItemActionType<T> type,
      LivingExtension<?, ?> performer, T selectedTarget) {
    super(hand);
    this.type = type;
    this.performer = performer;
    this.selectedTarget = selectedTarget;
  }

  public T getSelectedTarget() {
    return this.selectedTarget;
  }

  @Override
  public boolean start(boolean simulate) {
    return this.selectedTarget != null && super.start(simulate);
  }

  @Override
  public ActionObserver createPerformerObserver() {
    return ActionObserver.create(this, ProgressBar.create(this.getType(),
        this.performer == this.selectedTarget ? null
            : new TranslatableComponent("action.target",
                this.selectedTarget.getEntity().getDisplayName().getString()),
        this::getProgress));
  }

  @Override
  public ActionObserver createTargetObserver() {
    return ActionObserver.create(this, ProgressBar.create(this.getType(),
        new TranslatableComponent("action.performer",
            this.performer.getEntity().getDisplayName().getString()),
        this::getProgress));
  }

  @Override
  public boolean tick() {
    if (this.selectedTarget != this.performer) {
      var result = RayTraceUtil.rayTraceEntities(this.performer.getEntity()).orElse(null);
      if (result == null || result.getEntity() != this.selectedTarget.getEntity()) {
        this.performer.cancelAction(true);
        return false;
      }
    }
    return super.tick();
  }

  @Override
  public void stop(StopReason reason) {
    super.stop(reason);

    if (!reason.isCompleted()) {
      return;
    }

    if (this.type.getCustomAction() != null
        && performer.getEntity().getRandom().nextFloat() < this.type.getCustomAction().chance()) {
      this.type.getCustomAction().consumer().accept(this.performer, this.selectedTarget);
    }

    this.selectedTarget.getEntity().curePotionEffects(this.performer.getMainHandItem());

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
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }

  @Override
  public Optional<LivingExtension<?, ?>> getTarget() {
    return this.selectedTarget == this.performer
        ? Optional.empty()
        : Optional.of(this.selectedTarget);
  }

  @Override
  public ItemActionType<?> getType() {
    return this.type;
  }
}
