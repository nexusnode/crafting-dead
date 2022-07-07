/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.action.item;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
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
    return ActionObserver.create(this, ProgressBar.create(this.type(),
        this.performer == this.selectedTarget ? null
            : new TranslatableComponent("action.target",
                this.selectedTarget.entity().getDisplayName().getString()),
        this::getProgress));
  }

  @Override
  public ActionObserver createTargetObserver() {
    return ActionObserver.create(this, ProgressBar.create(this.type(),
        new TranslatableComponent("action.performer",
            this.performer.entity().getDisplayName().getString()),
        this::getProgress));
  }

  @Override
  public boolean tick() {
    if (this.selectedTarget != this.performer) {
      var result = RayTraceUtil.rayTraceEntities(this.performer.entity()).orElse(null);
      if (result == null || result.getEntity() != this.selectedTarget.entity()) {
        this.performer.cancelAction(true);
        return false;
      }
    }
    return super.tick();
  }

  @Override
  public void stop(StopReason reason) {
    if (reason.isCompleted()) {
      if (this.type.getCustomAction() != null
          && performer.entity().getRandom().nextFloat() < this.type.getCustomAction().chance()) {
        this.type.getCustomAction().consumer().accept(this.performer, this.selectedTarget);
      }

      this.selectedTarget.entity().curePotionEffects(this.getItemStack());

      for (var action : this.type.getEffects()) {
        if (performer.entity().getRandom().nextFloat() < action.chance()) {
          var effectInstance = action.effect().get();
          if (effectInstance.getEffect().isInstantenous()) {
            effectInstance.getEffect().applyInstantenousEffect(this.selectedTarget.entity(),
                this.selectedTarget.entity(),
                this.selectedTarget.entity(), effectInstance.getAmplifier(), 1.0D);
          } else {
            this.selectedTarget.entity().addEffect(new MobEffectInstance(effectInstance));
          }
        }
      }
    }

    super.stop(reason);
  }

  @Override
  public LivingExtension<?, ?> performer() {
    return this.performer;
  }

  @Override
  public Optional<LivingExtension<?, ?>> target() {
    return this.selectedTarget == this.performer
        ? Optional.empty()
        : Optional.ofNullable(this.selectedTarget);
  }

  @Override
  public ItemActionType<?> type() {
    return this.type;
  }
}
