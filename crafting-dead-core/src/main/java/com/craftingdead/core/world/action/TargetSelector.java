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
import java.util.function.Function;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TargetSelector {

  TargetSelector SELF_ONLY = (performer, target) -> Optional.of(performer);
  TargetSelector OTHERS_ONLY = (performer, target) -> Optional.ofNullable(target);
  TargetSelector SELF_OR_OTHERS =
      (performer, target) -> Optional.ofNullable(target == null ? performer : target);

  Optional<LivingExtension<?, ?>> select(LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target);

  default TargetSelector ofType(EntityType<?> entityType) {
    return this.andThen(
        target -> Optional.ofNullable(target.getEntity().getType() == entityType ? target : null));
  }

  default TargetSelector ofType(Class<? extends LivingEntity> entityType) {
    return this.andThen(
        target -> Optional.ofNullable(
            target != null && entityType.isInstance(target.getEntity()) ? target : null));
  }

  default TargetSelector andThen(
      Function<LivingExtension<?, ?>, Optional<LivingExtension<?, ?>>> after) {
    Objects.requireNonNull(after);
    return (performer, target) -> after.apply(this.select(performer, target).orElse(null));
  }
}
