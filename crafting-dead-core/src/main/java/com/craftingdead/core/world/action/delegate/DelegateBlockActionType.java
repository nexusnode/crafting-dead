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

import java.util.Optional;
import java.util.function.Predicate;
import com.craftingdead.core.world.action.Action;
import net.minecraft.world.level.block.state.BlockState;

public final class DelegateBlockActionType extends AbstractDelegateActionType {

  private final Predicate<BlockState> predicate;

  private DelegateBlockActionType(Builder builder) {
    super(builder);
    this.predicate = builder.predicate;
  }

  @Override
  public Optional<? extends DelegateAction> create(Action action) {
    return Optional.of(new DelegatedBlockAction(this));
  }

  public Predicate<BlockState> getPredicate() {
    return this.predicate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends AbstractDelegateActionType.Builder<Builder> {

    private Predicate<BlockState> predicate;

    private Builder() {
      super(DelegateBlockActionType::new);
    }

    public Builder forBlock(Predicate<BlockState> predicate) {
      this.predicate = predicate;
      return this;
    }
  }
}
