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

package com.craftingdead.core.world.item.gun.aimable;

import java.util.Set;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunItem;
import com.craftingdead.core.world.item.scope.Scope;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class AimableGunItem extends GunItem {

  /**
   * Whether the gun has bolt action
   */
  private final boolean boltAction;

  protected AimableGunItem(Builder builder) {
    super(builder);
    this.boltAction = builder.boltAction;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, CompoundTag nbt) {
    return new SerializableCapabilityProvider<>(
        LazyOptional.of(() -> AimableGun.create(AimableGunClient::new, itemStack, this)),
        Set.of(() -> Gun.CAPABILITY, () -> CombatSlotProvider.CAPABILITY, () -> Scope.CAPABILITY),
        CompoundTag::new);
  }

  public boolean hasBoltAction() {
    return this.boltAction;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends GunItem.Builder<Builder> {

    private boolean boltAction = false;

    private Builder() {
      super(AimableGunItem::new);
    }

    public Builder setBoltAction(boolean boltAction) {
      this.boltAction = boltAction;
      return this;
    }
  }
}
