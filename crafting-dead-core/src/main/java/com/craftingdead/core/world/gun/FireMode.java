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

package com.craftingdead.core.world.gun;

import java.util.Optional;
import javax.annotation.Nullable;

public enum FireMode {

  AUTO("auto"), BURST("burst", 3), SEMI("semi", 1);

  private final String name;
  @Nullable
  private final Integer maxShots;

  private FireMode(String name) {
    this(name, null);
  }

  private FireMode(String name, @Nullable Integer maxShots) {
    this.name = name;
    this.maxShots = maxShots;
  }

  public String getTranslationKey() {
    return "fire_mode." + this.name;
  }

  /**
   * The max shots this {@link FireMode} allows to fire in a row.
   */
  public Optional<Integer> getMaxShots() {
    return Optional.ofNullable(this.maxShots);
  }
}
