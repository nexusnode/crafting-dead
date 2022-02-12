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

package com.craftingdead.core.world.item.gun.ammoprovider;

import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class MagazineAmmoProvider implements AmmoProvider {

  private ItemStack magazineStack;
  private boolean stackChanged;

  public MagazineAmmoProvider() {
    this(ItemStack.EMPTY);
  }

  public MagazineAmmoProvider(ItemStack magazineStack) {
    this.magazineStack = magazineStack;
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.put("magazineStack", this.magazineStack.serializeNBT());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    if (nbt.contains("magazineStack", Tag.TAG_COMPOUND)) {
      this.magazineStack = ItemStack.of(nbt.getCompound("magazineStack"));
      this.stackChanged = true;
    }
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    if (this.stackChanged || writeAll) {
      out.writeBoolean(true);
      out.writeItem(this.magazineStack);
      this.stackChanged = false;
    } else {
      out.writeBoolean(false);
    }
    this.getMagazine().ifPresent(magazine -> magazine.encode(out, writeAll));
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    if (in.readBoolean()) {
      this.magazineStack = in.readItem();
    }
    this.getMagazine().ifPresent(magazine -> magazine.decode(in));
  }

  @Override
  public boolean requiresSync() {
    return this.getMagazine().map(Magazine::requiresSync).orElse(false) || this.stackChanged;
  }

  @Override
  public void reload(LivingExtension<?, ?> living) {
    living.performAction(ActionTypes.MAGAZINE_RELOAD.get().decode(living, null), true);
  }

  @Override
  public void unload(LivingExtension<?, ?> living) {
    living.performAction(ActionTypes.REMOVE_MAGAZINE.get().decode(living, null), true);
  }

  @Override
  public int getReserveSize() {
    return 0;
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.magazineStack;
  }

  public void setMagazineStack(ItemStack magazineStack) {
    this.magazineStack = magazineStack;
    this.stackChanged = true;
  }

  @Override
  public AmmoProviderType getType() {
    return AmmoProviderTypes.MAGAZINE.get();
  }
}
