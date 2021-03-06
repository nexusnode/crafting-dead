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

import com.craftingdead.core.capability.actionprovider.DefaultActionProvider;
import com.craftingdead.core.capability.actionprovider.IActionProvider;
import com.craftingdead.core.capability.animationprovider.DefaultAnimationProvider;
import com.craftingdead.core.capability.animationprovider.IAnimationProvider;
import com.craftingdead.core.capability.clothing.DefaultClothing;
import com.craftingdead.core.capability.clothing.IClothing;
import com.craftingdead.core.capability.combatitem.ICombatItem;
import com.craftingdead.core.capability.gun.GunImpl;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.hat.DefaultHat;
import com.craftingdead.core.capability.hat.IHat;
import com.craftingdead.core.capability.hydration.DefaultHydration;
import com.craftingdead.core.capability.hydration.IHydration;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.living.LivingImpl;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.capability.magazine.MagazineImpl;
import com.craftingdead.core.capability.paint.DefaultPaint;
import com.craftingdead.core.capability.paint.IPaint;
import com.craftingdead.core.capability.scope.IScope;
import com.craftingdead.core.capability.storage.DefaultStorage;
import com.craftingdead.core.capability.storage.IStorage;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ModCapabilities {

  @CapabilityInject(ILiving.class)
  public static final Capability<ILiving<?, ?>> LIVING = null;

  @CapabilityInject(IGun.class)
  public static final Capability<IGun> GUN = null;

  @CapabilityInject(IPaint.class)
  public static final Capability<IPaint> PAINT = null;

  @CapabilityInject(IMagazine.class)
  public static final Capability<IMagazine> MAGAZINE = null;

  @CapabilityInject(IStorage.class)
  public static final Capability<IStorage> STORAGE = null;

  @CapabilityInject(IScope.class)
  public static final Capability<IScope> SCOPE = null;

  @CapabilityInject(IHydration.class)
  public static final Capability<IHydration> HYDRATION = null;

  @CapabilityInject(IClothing.class)
  public static final Capability<IClothing> CLOTHING = null;

  @CapabilityInject(IHat.class)
  public static final Capability<IHat> HAT = null;

  @CapabilityInject(IActionProvider.class)
  public static final Capability<IActionProvider> ACTION_PROVIDER = null;

  @CapabilityInject(IAnimationProvider.class)
  public static final Capability<IAnimationProvider<?>> ANIMATION_PROVIDER = null;

  @CapabilityInject(ICombatItem.class)
  public static final Capability<ICombatItem> COMBAT_ITEM = null;

  static {
    ModCapabilities.registerCapabilities();
  }

  private static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(ILiving.class, new EmptyStorage<>(), LivingImpl::new);
    CapabilityManager.INSTANCE.register(IGun.class, new EmptyStorage<>(), GunImpl::new);
    CapabilityManager.INSTANCE.register(IPaint.class, new EmptyStorage<>(), DefaultPaint::new);
    CapabilityManager.INSTANCE
        .register(IMagazine.class, new EmptyStorage<>(), MagazineImpl::new);
    CapabilityManager.INSTANCE.register(IStorage.class, new EmptyStorage<>(), DefaultStorage::new);
    CapabilityManager.INSTANCE.register(IScope.class, new EmptyStorage<>(),
        ModCapabilities::unsupported);
    CapabilityManager.INSTANCE
        .register(IHydration.class, new EmptyStorage<>(), DefaultHydration::new);
    CapabilityManager.INSTANCE
        .register(IClothing.class, new EmptyStorage<>(), DefaultClothing::new);
    CapabilityManager.INSTANCE.register(IHat.class, new EmptyStorage<>(), DefaultHat::new);
    CapabilityManager.INSTANCE.register(IActionProvider.class, new EmptyStorage<>(),
        DefaultActionProvider::new);
    CapabilityManager.INSTANCE.register(IAnimationProvider.class, new EmptyStorage<>(),
        DefaultAnimationProvider::new);
    CapabilityManager.INSTANCE.register(ICombatItem.class, new EmptyStorage<>(),
        ModCapabilities::unsupported);
  }

  private static <T> T unsupported() {
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("unchecked")
  public static <T, R extends T> R getExpected(Capability<T> capability,
      ICapabilityProvider provider, Class<R> clazz) {
    return provider.getCapability(capability)
        .filter(t -> clazz.isInstance(t))
        .map(t -> (R) t)
        .orElseThrow(
            () -> new IllegalStateException("Expecting capability '" + capability.getName()));
  }

  private static class EmptyStorage<C> implements Capability.IStorage<C> {

    @Override
    public INBT writeNBT(Capability<C> capability, C instance, Direction side) {
      return null;
    }

    @Override
    public void readNBT(Capability<C> capability, C instance, Direction side, INBT nbt) {}
  }
}
