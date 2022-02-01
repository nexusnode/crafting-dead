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

package com.craftingdead.core.world.action;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TargetSelector<T extends LivingExtension<?, ?>> {

  TargetSelector<?> SELF_ONLY = (performer, target) -> Optional.of(performer);
  TargetSelector<?> OTHERS_ONLY = (performer, target) -> Optional.ofNullable(target);
  TargetSelector<?> SELF_OR_OTHERS =
      (performer, target) -> Optional.ofNullable(target == null ? performer : target);

  Optional<T> select(LivingExtension<?, ?> performer, @Nullable LivingExtension<?, ?> target);

  default TargetSelector<PlayerExtension<?>> players() {
    return (performer, target) -> this.select(performer, target)
        .filter(l -> l instanceof PlayerExtension)
        .map(l -> (PlayerExtension<?>) l);
  }

  @SuppressWarnings("unchecked")
  default <E extends LivingEntity> TargetSelector<LivingExtension<E, ?>> ofEntityType(
      EntityType<E> type) {
    Objects.requireNonNull(type);
    return (performer, target) -> this.select(performer, target)
        .filter(living -> living.getEntity().getType() == type)
        .map(living -> (LivingExtension<E, ?>) living);
  }

  @SuppressWarnings("unchecked")
  default <E extends LivingEntity> TargetSelector<LivingExtension<E, ?>> ofEntityType(
      Class<E> clazz) {
    Objects.requireNonNull(clazz);
    return (performer, target) -> this.select(performer, target)
        .filter(living -> clazz.isInstance(living.getEntity()))
        .map(living -> (LivingExtension<E, ?>) living);
  }

  default TargetSelector<?> hasEffect(Supplier<MobEffect> effect) {
    return this.filter(living -> living.getEntity().hasEffect(effect.get()));
  }

  default TargetSelector<T> filter(Predicate<T> predicate) {
    Objects.requireNonNull(predicate);
    return (performer, target) -> this.select(performer, target).filter(predicate);
  }
}
