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

package com.craftingdead.core.world.item.gun;

import java.util.function.Function;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SimpleGunType extends GunItem {

  protected SimpleGunType(Builder<?> builder) {
    super(builder);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, CompoundTag nbt) {
    return CapabilityUtil.serializableProvider(
        () -> TypedGun.create(this.getClientFactory(), itemStack, this),
        Gun.CAPABILITY, CombatSlotProvider.CAPABILITY);
  }

  protected <T extends TypedGun<?>> Function<T, TypedGunClient<? super T>> getClientFactory() {
    return TypedGunClient::new;
  }

  public static Builder<?> builder() {
    return new Builder<>(SimpleGunType::new);
  }
}
