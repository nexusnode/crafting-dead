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

import com.craftingdead.core.action.RemoveMagazineAction;
import com.craftingdead.core.action.reload.MagazineReloadAction;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.magazine.IMagazine;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;

public class MagazineAmmoProvider implements IAmmoProvider {

  private ItemStack magazineStack;
  private boolean stackChanged;

  public MagazineAmmoProvider() {
    this(ItemStack.EMPTY);
  }

  public MagazineAmmoProvider(ItemStack magazineStack) {
    this.magazineStack = magazineStack;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.put("magazineStack", this.magazineStack.serializeNBT());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    if (nbt.contains("magazineStack", Constants.NBT.TAG_COMPOUND)) {
      this.magazineStack = ItemStack.read(nbt.getCompound("magazineStack"));
      this.stackChanged = true;
    }
  }

  @Override
  public void encode(PacketBuffer out, boolean writeAll) {
    if (this.stackChanged || writeAll) {
      out.writeBoolean(true);
      out.writeItemStack(this.magazineStack);
      this.stackChanged = false;
    } else {
      out.writeBoolean(false);
    }
    this.getMagazine().ifPresent(magazine -> magazine.encode(out, writeAll));
  }

  @Override
  public void decode(PacketBuffer in) {
    if (in.readBoolean()) {
      this.magazineStack = in.readItemStack();
    }
    this.getMagazine().ifPresent(magazine -> magazine.decode(in));
  }

  @Override
  public boolean requiresSync() {
    return this.getMagazine().map(IMagazine::requiresSync).orElse(false) || this.stackChanged;
  }

  @Override
  public void reload(ILiving<?, ?> living) {
    living.performAction(new MagazineReloadAction(living), true);
  }

  @Override
  public void unload(ILiving<?, ?> living) {
    living.performAction(new RemoveMagazineAction(living), true);
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
