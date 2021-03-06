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

package com.craftingdead.core.capability.animationprovider;

import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeCallable;

public class DefaultAnimationProvider<T extends IAnimationController>
    implements IAnimationProvider<T> {

  @Nullable
  private final Supplier<DistExecutor.SafeCallable<T>> factory;

  private T animationController;

  public DefaultAnimationProvider() {
    this(null);
  }

  public DefaultAnimationProvider(Supplier<SafeCallable<T>> factory) {
    this.factory = factory;
  }

  @Override
  public Optional<T> getAnimationController() {
    return Optional.ofNullable(this.animationController == null
        ? this.animationController = DistExecutor.safeCallWhenOn(Dist.CLIENT, this.factory)
        : this.animationController);
  }
}
