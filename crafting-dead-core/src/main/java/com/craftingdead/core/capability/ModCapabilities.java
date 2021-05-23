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

import com.craftingdead.core.client.animation.AnimationProvider;
import com.craftingdead.core.client.animation.DefaultAnimationProvider;
import com.craftingdead.core.world.clothing.Clothing;
import com.craftingdead.core.world.clothing.DefaultClothing;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.LivingExtensionImpl;
import com.craftingdead.core.world.gun.IGun;
import com.craftingdead.core.world.gun.magazine.IMagazine;
import com.craftingdead.core.world.gun.magazine.MagazineImpl;
import com.craftingdead.core.world.gun.paint.DefaultPaint;
import com.craftingdead.core.world.gun.paint.IPaint;
import com.craftingdead.core.world.hat.DefaultHat;
import com.craftingdead.core.world.hat.Hat;
import com.craftingdead.core.world.inventory.storage.ItemStackHandlerStorage;
import com.craftingdead.core.world.inventory.storage.Storage;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.hydration.DefaultHydration;
import com.craftingdead.core.world.item.hydration.Hydration;
import com.craftingdead.core.world.item.scope.Scope;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ModCapabilities {

  @CapabilityInject(LivingExtension.class)
  public static final Capability<LivingExtension<?, ?>> LIVING = null;

  @CapabilityInject(Storage.class)
  public static final Capability<Storage> STORAGE = null;

  @CapabilityInject(Clothing.class)
  public static final Capability<Clothing> CLOTHING = null;

  @CapabilityInject(Hat.class)
  public static final Capability<Hat> HAT = null;

  @CapabilityInject(AnimationProvider.class)
  public static final Capability<AnimationProvider<?>> ANIMATION_PROVIDER = null;

  @CapabilityInject(IGun.class)
  public static final Capability<IGun> GUN = null;

  @CapabilityInject(IPaint.class)
  public static final Capability<IPaint> PAINT = null;

  @CapabilityInject(Scope.class)
  public static final Capability<Scope> SCOPE = null;

  @CapabilityInject(CombatSlotProvider.class)
  public static final Capability<CombatSlotProvider> COMBAT_SLOT_PROVIDER = null;

  @CapabilityInject(IMagazine.class)
  public static final Capability<IMagazine> MAGAZINE = null;

  static {
    CapabilityManager.INSTANCE.register(LivingExtension.class, new EmptyStorage<>(),
        LivingExtensionImpl::new);
    CapabilityManager.INSTANCE.register(Storage.class, new EmptyStorage<>(),
        ItemStackHandlerStorage::new);
    CapabilityManager.INSTANCE.register(Hydration.class, new EmptyStorage<>(),
        DefaultHydration::new);
    CapabilityManager.INSTANCE.register(Clothing.class, new EmptyStorage<>(),
        DefaultClothing::new);
    CapabilityManager.INSTANCE.register(Hat.class, new EmptyStorage<>(), DefaultHat::new);
    CapabilityManager.INSTANCE.register(AnimationProvider.class, new EmptyStorage<>(),
        DefaultAnimationProvider::new);
    CapabilityManager.INSTANCE.register(CombatSlotProvider.class, new EmptyStorage<>(),
        ModCapabilities::unsupported);
    CapabilityManager.INSTANCE.register(IGun.class, EmptyStorage.getInstance(),
        ModCapabilities::unsupported);
    CapabilityManager.INSTANCE.register(IPaint.class, EmptyStorage.getInstance(),
        DefaultPaint::new);
    CapabilityManager.INSTANCE.register(IMagazine.class, EmptyStorage.getInstance(),
        MagazineImpl::new);
    CapabilityManager.INSTANCE.register(Scope.class, new EmptyStorage<>(),
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
