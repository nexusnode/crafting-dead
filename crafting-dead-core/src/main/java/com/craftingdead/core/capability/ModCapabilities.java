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

import com.craftingdead.core.clothing.DefaultClothing;
import com.craftingdead.core.clothing.IClothing;
import com.craftingdead.core.hat.DefaultHat;
import com.craftingdead.core.hat.IHat;
import com.craftingdead.core.item.animation.DefaultAnimationProvider;
import com.craftingdead.core.item.animation.IAnimationProvider;
import com.craftingdead.core.item.combatslot.ICombatSlotProvider;
import com.craftingdead.core.item.gun.IGun;
import com.craftingdead.core.item.gun.magazine.IMagazine;
import com.craftingdead.core.item.gun.magazine.MagazineImpl;
import com.craftingdead.core.item.gun.paint.DefaultPaint;
import com.craftingdead.core.item.gun.paint.IPaint;
import com.craftingdead.core.item.hydration.DefaultHydration;
import com.craftingdead.core.item.hydration.IHydration;
import com.craftingdead.core.item.scope.IScope;
import com.craftingdead.core.living.ILiving;
import com.craftingdead.core.living.LivingImpl;
import com.craftingdead.core.storage.DefaultStorage;
import com.craftingdead.core.storage.IStorage;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ModCapabilities {

  @CapabilityInject(ILiving.class)
  public static final Capability<ILiving<?, ?>> LIVING = null;

  @CapabilityInject(IStorage.class)
  public static final Capability<IStorage> STORAGE = null;

  @CapabilityInject(IHydration.class)
  public static final Capability<IHydration> HYDRATION = null;

  @CapabilityInject(IClothing.class)
  public static final Capability<IClothing> CLOTHING = null;

  @CapabilityInject(IHat.class)
  public static final Capability<IHat> HAT = null;

  @CapabilityInject(IAnimationProvider.class)
  public static final Capability<IAnimationProvider<?>> ANIMATION_PROVIDER = null;

  @CapabilityInject(IGun.class)
  public static final Capability<IGun> GUN = null;

  @CapabilityInject(IPaint.class)
  public static final Capability<IPaint> PAINT = null;

  @CapabilityInject(IScope.class)
  public static final Capability<IScope> SCOPE = null;

  @CapabilityInject(ICombatSlotProvider.class)
  public static final Capability<ICombatSlotProvider> COMBAT_SLOT_PROVIDER = null;

  @CapabilityInject(IMagazine.class)
  public static final Capability<IMagazine> MAGAZINE = null;

  static {
    CapabilityManager.INSTANCE.register(ILiving.class, new EmptyStorage<>(), LivingImpl::new);
    CapabilityManager.INSTANCE.register(IStorage.class, new EmptyStorage<>(),
        DefaultStorage::new);
    CapabilityManager.INSTANCE.register(IHydration.class, new EmptyStorage<>(),
        DefaultHydration::new);
    CapabilityManager.INSTANCE.register(IClothing.class, new EmptyStorage<>(),
        DefaultClothing::new);
    CapabilityManager.INSTANCE.register(IHat.class, new EmptyStorage<>(), DefaultHat::new);
    CapabilityManager.INSTANCE.register(IAnimationProvider.class, new EmptyStorage<>(),
        DefaultAnimationProvider::new);
    CapabilityManager.INSTANCE.register(ICombatSlotProvider.class, new EmptyStorage<>(),
        ModCapabilities::unsupported);
    CapabilityManager.INSTANCE.register(IGun.class, EmptyStorage.getInstance(),
        ModCapabilities::unsupported);
    CapabilityManager.INSTANCE.register(IPaint.class, EmptyStorage.getInstance(),
        DefaultPaint::new);
    CapabilityManager.INSTANCE.register(IMagazine.class, EmptyStorage.getInstance(),
        MagazineImpl::new);
    CapabilityManager.INSTANCE.register(IScope.class, new EmptyStorage<>(),
        ModCapabilities::unsupported);
  }

  public static <T> T unsupported() {
    throw new UnsupportedOperationException();
  }

  public static <T, R extends T> R getExpected(Capability<T> capability,
      ICapabilityProvider provider, Class<R> clazz) {
    return provider.getCapability(capability)
        .filter(clazz::isInstance)
        .map(clazz::cast)
        .orElseThrow(
            () -> new IllegalStateException("Expecting capability: " + capability.getName()));
  }
}
