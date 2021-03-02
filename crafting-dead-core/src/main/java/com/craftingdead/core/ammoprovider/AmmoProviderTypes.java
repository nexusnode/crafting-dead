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

package com.craftingdead.core.ammoprovider;

import com.craftingdead.core.CraftingDead;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class AmmoProviderTypes {
  @SuppressWarnings("unchecked")
  public static final DeferredRegister<AmmoProviderType> AMMO_PROVIDER_TYPES =
      DeferredRegister.create((Class<AmmoProviderType>) (Class<?>) AmmoProviderType.class,
          CraftingDead.ID);

  public static final RegistryObject<AmmoProviderType> MAGAZINE =
      AMMO_PROVIDER_TYPES.register("magazine",
          () -> new AmmoProviderType(MagazineAmmoProvider::new));

  public static final RegistryObject<AmmoProviderType> REFILLABLE =
      AMMO_PROVIDER_TYPES.register("refillable",
          () -> new AmmoProviderType(RefillableAmmoProvider::new));

}
