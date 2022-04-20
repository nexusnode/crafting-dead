/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun.ammoprovider;

import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

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
    living.performAction(ActionTypes.REFILLABLE_RELOAD.get().decode(living, null), true);
  }

  @Override
  public void unload(LivingExtension<?, ?> living) {}

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.put("magazineStack", this.magazineStack.serializeNBT());
    nbt.putInt("reserveSize", this.reserveSize);
    nbt.putBoolean("infiniteAmmo", this.infiniteAmmo);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    if (nbt.contains("magazineStack", Tag.TAG_COMPOUND)) {
      this.magazineStack = ItemStack.of(nbt.getCompound("magazineStack"));
    }
    this.reserveSize = nbt.getInt("reserveSize");
    this.infiniteAmmo = nbt.getBoolean("infiniteAmmo");
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
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
  public void decode(FriendlyByteBuf in) {
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
