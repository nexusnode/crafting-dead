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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.TargetSelector;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class EntityItemActionType<T extends LivingExtension<?, ?>>
    extends ItemActionType<EntityItemAction<T>> {

  private final TargetSelector<T> targetSelector;
  private final List<EffectAction> effects;
  @Nullable
  private final CustomAction<T> customAction;

  private EntityItemActionType(Builder<T> builder) {
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
  public void encode(EntityItemAction<T> action, FriendlyByteBuf out) {
    out.writeEnum(action.getHand());
    out.writeVarInt(action.getSelectedTarget().getEntity().getId());
  }

  @SuppressWarnings("unchecked")
  @Override
  public EntityItemAction<T> decode(LivingExtension<?, ?> performer, FriendlyByteBuf in) {
    var hand = in.readEnum(InteractionHand.class);
    var targetId = in.readVarInt();
    var target = performer.getLevel().getEntity(targetId);
    if (target instanceof LivingEntity livingTarget) {
      return new EntityItemAction<>(hand, this, performer,
          (T) LivingExtension.getOrThrow(livingTarget));
    } else {
      throw new IllegalStateException("Invalid target: " + target);
    }
  }

  @Override
  public Optional<Action> createEntityAction(LivingExtension<?, ?> performer,
      LivingExtension<?, ?> target, InteractionHand hand) {
    var selectedTarget = this.getTargetSelector().select(performer, target).orElse(null);
    return Optional.of(new EntityItemAction<>(hand, this, performer, selectedTarget));
  }

  @Override
  public Optional<Action> createAction(LivingExtension<?, ?> performer, InteractionHand hand) {
    var selectedTarget = this.getTargetSelector().select(performer, null).orElse(null);
    return Optional.of(new EntityItemAction<>(hand, this, performer, selectedTarget));
  }

  public static <T extends LivingExtension<?, ?>> Builder<T> builder(
      TargetSelector<T> targetSelector) {
    return new Builder<>(targetSelector);
  }

  public record EffectAction(Supplier<MobEffectInstance> effect, float chance) {}

  public record CustomAction<T extends LivingExtension<?, ?>> (
      BiConsumer<LivingExtension<?, ?>, T> consumer,
      float chance) {}

  public static final class Builder<T extends LivingExtension<?, ?>>
      extends ItemActionType.Builder<Builder<T>> {

    private final TargetSelector<T> targetSelector;
    private final List<EffectAction> effects = new ArrayList<>();
    @Nullable
    private CustomAction<T> customAction;

    private Builder(TargetSelector<T> targetSelector) {
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

    @Override
    public EntityItemActionType<?> build() {
      return new EntityItemActionType<>(this);
    }
  }
}
