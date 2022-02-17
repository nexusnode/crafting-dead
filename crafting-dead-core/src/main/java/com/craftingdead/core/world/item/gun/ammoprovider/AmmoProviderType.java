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

package com.craftingdead.core.world.item.gun.ammoprovider;

import java.util.function.Supplier;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class AmmoProviderType extends ForgeRegistryEntry<AmmoProviderType> {

  private final Supplier<AmmoProvider> factory;

  public AmmoProviderType(Supplier<AmmoProvider> factory) {
    this.factory = factory;
  }

  public AmmoProvider create() {
    return this.factory.get();
  }
}
