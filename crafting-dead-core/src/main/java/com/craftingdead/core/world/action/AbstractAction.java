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
