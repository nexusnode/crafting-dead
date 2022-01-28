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

import java.util.Optional;
import javax.annotation.Nullable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public abstract class AbstractAction<T extends ActionType> implements Action {

  private final T type;
  private final LivingExtension<?, ?> performer;
  @Nullable
  private final LivingExtension<?, ?> target;

  public AbstractAction(T type, LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target) {
    this.type = type;
    this.performer = performer;
    this.target = target;
  }

  @Override
  public T getType() {
    return this.type;
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }

  @Override
  public LivingExtension.ProgressMonitor getPerformerProgress() {
    return new ActionProgress(true);
  }

  @Override
  public Optional<LivingExtension<?, ?>> getTarget() {
    return Optional.ofNullable(this.target);
  }

  @Override
  public LivingExtension.ProgressMonitor getTargetProgress() {
    return new ActionProgress(false);
  }

  protected String getTranslationKey() {
    return Util.makeDescriptionId("action", AbstractAction.this.type.getRegistryName());
  }

  protected abstract float getProgress(float partialTicks);

  private class ActionProgress implements LivingExtension.ProgressMonitor {

    private final boolean performer;

    public ActionProgress(boolean performer) {
      this.performer = performer;
    }

    @Override
    public Component getMessage() {
      return new TranslatableComponent(AbstractAction.this.getTranslationKey() + ".message");
    }

    @Override
    public Optional<Component> getSubMessage() {
      return this.performer
          ? AbstractAction.this.target == null
              ? Optional.empty()
              : Optional.of(new TranslatableComponent("action.target",
                  AbstractAction.this.target.getEntity().getDisplayName().getString()))
          : Optional.of(new TranslatableComponent("action.performer",
              AbstractAction.this.performer.getEntity().getDisplayName().getString()));
    }

    @Override
    public float getProgress(float partialTicks) {
      return AbstractAction.this.getProgress(partialTicks);
    }

    @Override
    public void stop() {
      AbstractAction.this.getPerformer().cancelAction(true);
    }
  }
}
