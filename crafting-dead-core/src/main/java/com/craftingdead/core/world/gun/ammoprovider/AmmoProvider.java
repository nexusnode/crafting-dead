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

package com.craftingdead.core.world.gun.ammoprovider;

import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.network.BufferSerializable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.magazine.Magazine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface AmmoProvider extends INBTSerializable<CompoundNBT>, BufferSerializable {

  void reload(LivingExtension<?, ?> living);

  void unload(LivingExtension<?, ?> living);

  int getReserveSize();

  ItemStack getMagazineStack();

  default Magazine getExpectedMagazine() {
    return this.getMagazine()
        .orElseThrow(() -> new IllegalStateException("No magazine capability"));
  }

  default LazyOptional<Magazine> getMagazine() {
    return this.getMagazineStack().getCapability(Capabilities.MAGAZINE);
  }

  AmmoProviderType getType();
}
