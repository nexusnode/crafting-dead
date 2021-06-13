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

package com.craftingdead.core.world.gun.type.simple;

import java.util.function.Function;
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.world.gun.type.AbstractGunType;
import com.craftingdead.core.world.gun.type.TypedGun;
import com.craftingdead.core.world.gun.type.TypedGunClient;
import com.google.common.collect.ImmutableSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SimpleGunType extends AbstractGunType {

  protected SimpleGunType(Builder<?> builder) {
    super(builder);
  }

  @Override
  public ICapabilityProvider createCapabilityProvider(ItemStack itemStack) {
    return new SerializableCapabilityProvider<>(
        LazyOptional.of(() -> new TypedGun<>(this.getClientFactory(), itemStack, this)),
        ImmutableSet.of(() -> Capabilities.GUN, () -> Capabilities.COMBAT_SLOT_PROVIDER),
        CompoundNBT::new);
  }

  protected <T extends TypedGun<?>> Function<T, TypedGunClient<? super T>> getClientFactory() {
    return TypedGunClient::new;
  }

  public static Builder<?> builder() {
    return new Builder<>(SimpleGunType::new);
  }
}
