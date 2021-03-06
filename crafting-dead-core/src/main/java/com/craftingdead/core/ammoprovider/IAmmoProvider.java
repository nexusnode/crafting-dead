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

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.util.IBufferSerializable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IAmmoProvider extends INBTSerializable<CompoundNBT>, IBufferSerializable {

  void reload(ILiving<?, ?> living);

  void unload(ILiving<?, ?> living);

  int getReserveSize();

  ItemStack getMagazineStack();

  default IMagazine getExpectedMagazine() {
    return this.getMagazine()
        .orElseThrow(() -> new IllegalStateException("No magazine capability"));
  }

  default LazyOptional<IMagazine> getMagazine() {
    return this.getMagazineStack().getCapability(ModCapabilities.MAGAZINE);
  }

  AmmoProviderType getType();
}
