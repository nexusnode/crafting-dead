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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.TargetSelector;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.effect.MobEffectInstance;

public final class DelegateEntityActionType<T extends LivingExtension<?, ?>>
    extends AbstractDelegateActionType {

  private final TargetSelector<T> targetSelector;
  private final List<EffectAction> effects;
  @Nullable
  private final CustomAction<T> customAction;

  private DelegateEntityActionType(Builder<T> builder) {
    super(builder);
    this.targetSelector = builder.targetSelector;
    this.effects = builder.effects;
    this.customAction = builder.customAction;
  }

  public TargetSelector<T> getTargetSelector() {
    return this.targetSelector;
  }

  public List<EffectAction> getEffects() {
    return this.effects;
  }

  @Nullable
  public CustomAction<T> getCustomAction() {
    return this.customAction;
  }

  @Override
  public Optional<? extends DelegateAction> create(Action action) {
    return this.targetSelector.select(action.getPerformer(), action.getTarget().orElse(null))
        .map(target -> new DelegateEntityAction<>(this, target));
  }

  public static <T extends LivingExtension<?, ?>> Builder<T> builder(
      TargetSelector<T> targetSelector) {
    return new Builder<>(targetSelector);
  }

  public record EffectAction(Supplier<MobEffectInstance> effect, float chance) {
  }

  public record CustomAction<T extends LivingExtension<?, ?>> (
      BiConsumer<LivingExtension<?, ?>, T> consumer,
      float chance) {
  }

  public static final class Builder<T extends LivingExtension<?, ?>>
      extends AbstractDelegateActionType.Builder<Builder<T>> {

    private final TargetSelector<T> targetSelector;
    private final List<EffectAction> effects = new ArrayList<>();
    @Nullable
    private CustomAction<T> customAction;

    private Builder(TargetSelector<T> targetSelector) {
      super(DelegateEntityActionType::new);
      this.targetSelector = targetSelector;
    }

    public Builder<T> effect(Supplier<MobEffectInstance> effect) {
      return this.effect(effect, 1.0F);
    }

    public Builder<T> effect(Supplier<MobEffectInstance> effect, float chance) {
      this.effects.add(new EffectAction(effect, chance));
      return this;
    }

    public Builder<T> customAction(BiConsumer<LivingExtension<?, ?>, T> customAction) {
      return this.customAction(customAction, 1.0F);
    }

    public Builder<T> customAction(BiConsumer<LivingExtension<?, ?>, T> customAction,
        float chance) {
      this.customAction = new CustomAction<>(customAction, chance);
      return this;
    }
  }
}
