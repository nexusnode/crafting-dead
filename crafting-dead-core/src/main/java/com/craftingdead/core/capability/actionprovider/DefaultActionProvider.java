/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
package com.craftingdead.core.capability.actionprovider;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.craftingdead.core.action.ActionType;
import com.craftingdead.core.action.IAction;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.util.math.BlockPos;

public class DefaultActionProvider implements IActionProvider {

  @Nullable
  private final BiFunction<ILiving<?>, BlockPos, IAction> blockFactory;
  @Nullable
  private final BiFunction<ILiving<?>, ILiving<?>, IAction> entityFactory;

  public DefaultActionProvider() {
    this(null, null);
  }

  public DefaultActionProvider(Supplier<? extends ActionType<?>> actionType) {
    this(null, (performer, target) -> actionType.get().createAction(performer, target));
  }

  public DefaultActionProvider(@Nullable BiFunction<ILiving<?>, BlockPos, IAction> blockFactory,
      @Nullable BiFunction<ILiving<?>, ILiving<?>, IAction> entityFactory) {
    this.blockFactory = blockFactory;
    this.entityFactory = entityFactory;
  }

  @Override
  public Optional<IAction> getBlockAction(ILiving<?> performer, BlockPos blockPos) {
    return this.blockFactory == null ? Optional.empty()
        : Optional.of(this.blockFactory.apply(performer, blockPos));
  }

  @Override
  public Optional<IAction> getEntityAction(ILiving<?> performer, ILiving<?> target) {
    return this.entityFactory == null ? Optional.empty()
        : Optional.of(this.entityFactory.apply(performer, target));
  }
}
