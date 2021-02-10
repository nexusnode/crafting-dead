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

package com.craftingdead.core.action.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.util.RayTraceUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.EntityRayTraceResult;

public class EntityActionEntry extends AbstractActionEntry<EntityActionEntry.Properties> {

  private BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> targetSelector = TargetSelector.SELF_ONLY;
  private final List<Pair<EffectInstance, Float>> effects;
  @Nullable
  private final Pair<Consumer<ILiving<?, ?>>, Float> customAction;

  private ILiving<?, ?> selectedTarget;

  public EntityActionEntry(Properties properties) {
    super(properties);
    this.targetSelector = properties.targetSelector;
    this.effects = properties.effects;
    this.customAction = properties.customAction;
  }

  @Override
  public boolean canPerform(ILiving<?, ?> performer, @Nullable ILiving<?, ?> target,
      ItemStack heldStack) {
    if (this.selectedTarget == null) {
      this.selectedTarget = this.targetSelector.apply(performer, target);
      if (this.selectedTarget == null) {
        return false;
      }
    }

    if (!performer.getEntity().getEntityWorld().isRemote()) {
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
  public boolean finish(ILiving<?, ?> performer, @Nullable ILiving<?, ?> target,
      ItemStack heldStack) {
    if (this.customAction != null
        && UseItemAction.random.nextFloat() < this.customAction.getRight()) {
      this.customAction.getLeft().accept(this.selectedTarget);
    }

    this.selectedTarget.getEntity().curePotionEffects(heldStack);

    for (Pair<EffectInstance, Float> pair : this.effects) {
      if (pair.getLeft() != null && UseItemAction.random.nextFloat() < pair.getRight()) {
        EffectInstance effectInstance = pair.getLeft();
        if (effectInstance.getPotion().isInstant()) {
          effectInstance.getPotion().affectEntity(this.selectedTarget.getEntity(),
              this.selectedTarget.getEntity(),
              this.selectedTarget.getEntity(), effectInstance.getAmplifier(), 1.0D);
        } else {
          this.selectedTarget.getEntity().addPotionEffect(new EffectInstance(effectInstance));
        }
      }
    }

    return true;
  }

  public static enum TargetSelector implements BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> {
    SELF_ONLY((performer, target) -> performer), OTHERS_ONLY(
        (performer, target) -> target), SELF_AND_OTHERS(
            (performer, target) -> target == null ? performer : target);

    private final BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> targetGetter;

    private TargetSelector(BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> targetGetter) {
      this.targetGetter = targetGetter;
    }

    @Override
    public ILiving<?, ?> apply(ILiving<?, ?> performer, @Nullable ILiving<?, ?> target) {
      return this.targetGetter.apply(performer, target);
    }

    public BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> ofType(EntityType<?> entityType) {
      return this.andThen(target -> target.getEntity().getType() == entityType ? target : null);
    }

    public BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> ofType(
        Class<? extends LivingEntity> entityType) {
      return this.andThen(
          target -> target != null && entityType.isAssignableFrom(target.getEntity().getClass())
              ? target
              : null);
    }
  }

  public static class Properties extends AbstractActionEntry.Properties<Properties> {
    private BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> targetSelector =
        TargetSelector.SELF_ONLY;
    private final List<Pair<EffectInstance, Float>> effects = new ArrayList<>();
    @Nullable
    private Pair<Consumer<ILiving<?, ?>>, Float> customAction;

    public Properties setTargetSelector(
        BiFunction<ILiving<?, ?>, ILiving<?, ?>, ILiving<?, ?>> targetSelector) {
      this.targetSelector = targetSelector;
      return this;
    }

    public Properties addEffect(Pair<EffectInstance, Float> effect) {
      this.effects.add(effect);
      return this;
    }

    public Properties setCustomAction(Pair<Consumer<ILiving<?, ?>>, Float> customAction) {
      this.customAction = customAction;
      return this;
    }
  }
}
