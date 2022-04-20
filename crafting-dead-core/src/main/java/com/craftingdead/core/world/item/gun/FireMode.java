/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun;

import com.craftingdead.core.CraftingDead;
import java.util.Optional;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;

public enum FireMode {

  AUTO("auto"),
  BURST("burst", CraftingDead.serverConfig.burstfireShotsPerBurst::get),
  SEMI("semi", () -> 1);

  private final String name;
  @Nullable
  private final IntSupplier maxShots;

  private FireMode(String name) {
    this(name, null);
  }

  private FireMode(String name, @Nullable IntSupplier maxShots) {
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
    return maxShots == null ? Optional.empty() : Optional.of(maxShots.getAsInt());
  }
}
