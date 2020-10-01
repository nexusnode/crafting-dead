/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.item;

import java.util.Optional;

public enum FireMode {

  AUTO("message.fire_mode.auto"),
  BURST("message.fire_mode.burst", 3),
  SEMI("message.fire_mode.semi", 1);

  private final String translationKey;
  private final Optional<Integer> maxShotsOptional;

  private FireMode(String translationKey) {
    this(translationKey, null);
  }

  private FireMode(String translationKey, Integer maxShots) {
    this.translationKey = translationKey;
    this.maxShotsOptional = Optional.ofNullable(maxShots);
  }

  public String getTranslationKey() {
    return this.translationKey;
  }

  /**
   * The max shots this {@link FireMode} allows to fire in a row.
   */
  public Optional<Integer> getMaxShots() {
    return this.maxShotsOptional;
  }
}
