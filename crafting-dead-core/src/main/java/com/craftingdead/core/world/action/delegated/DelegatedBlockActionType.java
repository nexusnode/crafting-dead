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

package com.craftingdead.core.world.action.delegated;

import java.util.Optional;
import java.util.function.Predicate;
import com.craftingdead.core.world.action.Action;
import net.minecraft.world.level.block.state.BlockState;

public final class DelegatedBlockActionType extends AbstractDelegatedActionType {

  private final Predicate<BlockState> predicate;

  private DelegatedBlockActionType(Builder builder) {
    super(builder);
    this.predicate = builder.predicate;
  }

  @Override
  public Optional<? extends DelegatedAction> create(Action action) {
    return Optional.of(new DelegatedBlockAction(this));
  }

  public Predicate<BlockState> getPredicate() {
    return this.predicate;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends AbstractDelegatedActionType.Builder<Builder> {

    private Predicate<BlockState> predicate;

    private Builder() {
      super(DelegatedBlockActionType::new);
    }

    public Builder setPredicate(Predicate<BlockState> predicate) {
      this.predicate = predicate;
      return this;
    }
  }
}
