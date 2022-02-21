/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

package com.craftingdead.core.capability;

import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;

class SimpleCapabilityProvider<C> implements ICapabilityProvider {

  protected final LazyOptional<C> instance;
  protected final Set<Capability<? super C>> capabilities;

  @Nullable
  protected final NonNullFunction<C, ICapabilityProvider> instanceMapper;

  public SimpleCapabilityProvider(LazyOptional<C> instance,
      Set<Capability<? super C>> capabilities,
      @Nullable NonNullFunction<C, ICapabilityProvider> instanceMapper) {
    this.instance = instance;
    this.capabilities = capabilities;
    this.instanceMapper = instanceMapper;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (this.capabilities.contains(cap)) {
      return this.instance.cast();
    } else if (this.instanceMapper != null) {
      return this.instance
          .lazyMap(this.instanceMapper)
          .lazyMap(provider -> provider.getCapability(cap, side))
          .orElse(LazyOptional.empty());
    }
    return LazyOptional.empty();
  }
}
