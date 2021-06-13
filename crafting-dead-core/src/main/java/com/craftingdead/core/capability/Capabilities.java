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

package com.craftingdead.core.capability;

import com.craftingdead.core.world.clothing.Clothing;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.gun.magazine.Magazine;
import com.craftingdead.core.world.hat.Hat;
import com.craftingdead.core.world.inventory.storage.Storage;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.hydration.Hydration;
import com.craftingdead.core.world.item.scope.Scope;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class Capabilities {

  @CapabilityInject(LivingExtension.class)
  public static final Capability<LivingExtension<?, ?>> LIVING = null;

  @CapabilityInject(Storage.class)
  public static final Capability<Storage> STORAGE = null;

  @CapabilityInject(Clothing.class)
  public static final Capability<Clothing> CLOTHING = null;

  @CapabilityInject(Hat.class)
  public static final Capability<Hat> HAT = null;

  @CapabilityInject(Gun.class)
  public static final Capability<Gun> GUN = null;

  @CapabilityInject(Scope.class)
  public static final Capability<Scope> SCOPE = null;

  @CapabilityInject(CombatSlotProvider.class)
  public static final Capability<CombatSlotProvider> COMBAT_SLOT_PROVIDER = null;

  @CapabilityInject(Magazine.class)
  public static final Capability<Magazine> MAGAZINE = null;

  static {
    CapabilityManager.INSTANCE.register(LivingExtension.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(Storage.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(Hydration.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(Clothing.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(Hat.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(CombatSlotProvider.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(Gun.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(Magazine.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
    CapabilityManager.INSTANCE.register(Scope.class, EmptyStorage.getInstance(),
        Capabilities::unsupported);
  }

  public static <T> T unsupported() {
    throw new UnsupportedOperationException();
  }

  public static <T, R extends T> R getOrThrow(Capability<T> capability,
      ICapabilityProvider provider, Class<R> clazz) {
    return provider.getCapability(capability)
        .filter(clazz::isInstance)
        .map(clazz::cast)
        .orElseThrow(
            () -> new IllegalStateException("Expecting capability: " + capability.getName()));
  }
}
