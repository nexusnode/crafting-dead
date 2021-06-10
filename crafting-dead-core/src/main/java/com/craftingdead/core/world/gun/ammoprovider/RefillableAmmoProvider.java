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

import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.magazine.Magazine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;

public class RefillableAmmoProvider implements AmmoProvider {

  private ItemStack magazineStack;
  private int reserveSize;
  private boolean infiniteAmmo;

  private boolean reserveSizeChanged;

  public RefillableAmmoProvider() {
    this(ItemStack.EMPTY, 0, false);
  }

  public RefillableAmmoProvider(ItemStack magazineStack, int reserveMagazineCount,
      boolean infiniteAmmo) {
    this.magazineStack = magazineStack;
    this.reserveSize =
        this.getMagazine().map(Magazine::getMaxSize).orElse(0) * reserveMagazineCount;
    this.infiniteAmmo = infiniteAmmo;
  }

  public boolean hasInfiniteAmmo() {
    return this.infiniteAmmo;
  }

  public void refillMagazine() {
    Magazine magazine = this.getExpectedMagazine();
    this.moveAmmoToReserve(magazine.getSize());
    this.moveAmmoToMagazine(this.infiniteAmmo ? magazine.getMaxSize()
        : Math.min(magazine.getMaxSize(), this.reserveSize));
  }

  public boolean moveAmmoToMagazine(int amount) {
    if (!this.infiniteAmmo && this.reserveSize < amount) {
      return false;
    }

    Magazine magazine = this.getExpectedMagazine();
    int ammoToLoad = Math.min(magazine.getMaxSize(), amount);
    magazine.setSize(ammoToLoad);
    if (!this.infiniteAmmo) {
      this.reserveSize -= ammoToLoad;
      this.reserveSizeChanged = true;
    }
    return true;
  }

  public boolean moveAmmoToReserve(int amount) {
    Magazine magazine = this.getExpectedMagazine();
    int currentSize = magazine.getSize();
    if (currentSize < amount) {
      return false;
    }

    if (!this.infiniteAmmo) {
      this.reserveSize += amount;
      this.reserveSizeChanged = true;
    }

    magazine.setSize(currentSize - amount);
    return true;
  }

  @Override
  public void reload(LivingExtension<?, ?> living) {
    living.performAction(ActionTypes.REFILLABLE_RELOAD.get().createAction(living, null), true);
  }

  @Override
  public void unload(LivingExtension<?, ?> living) {}

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("magazineStack", this.magazineStack.serializeNBT());
    nbt.putInt("reserveSize", this.reserveSize);
    nbt.putBoolean("infiniteAmmo", this.infiniteAmmo);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    if (nbt.contains("magazineStack", Constants.NBT.TAG_COMPOUND)) {
      this.magazineStack = ItemStack.of(nbt.getCompound("magazineStack"));
    }
    this.reserveSize = nbt.getInt("reserveSize");
    this.infiniteAmmo = nbt.getBoolean("infiniteAmmo");
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    if (writeAll) {
      out.writeBoolean(true);
      out.writeItem(this.magazineStack);
      out.writeBoolean(this.infiniteAmmo);
    } else {
      out.writeBoolean(false);
    }
    this.getExpectedMagazine().encode(out, writeAll);

    out.writeVarInt(this.reserveSize);
    this.reserveSizeChanged = false;
  }

  @Override
  public void decode(PacketBuffer in) {
    if (in.readBoolean()) {
      this.magazineStack = in.readItem();
      this.infiniteAmmo = in.readBoolean();
    }

    this.getExpectedMagazine().decode(in);
    this.reserveSize = in.readVarInt();
  }

  @Override
  public boolean requiresSync() {
    return this.reserveSizeChanged || this.getMagazine().map(Magazine::requiresSync).orElse(false);
  }

  @Override
  public int getReserveSize() {
    return this.reserveSize;
  }

  @Override
  public ItemStack getMagazineStack() {
    return this.magazineStack;
  }

  @Override
  public AmmoProviderType getType() {
    return AmmoProviderTypes.REFILLABLE.get();
  }
}
