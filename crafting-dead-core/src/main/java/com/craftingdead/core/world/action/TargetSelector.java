package com.craftingdead.core.world.action;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

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
