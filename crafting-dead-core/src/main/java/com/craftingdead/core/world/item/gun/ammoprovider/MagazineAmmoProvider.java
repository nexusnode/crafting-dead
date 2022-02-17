/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
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
    living.performAction(ActionTypes.MAGAZINE_RELOAD.get().createAction(living, null), true);
  }

  @Override
  public void unload(LivingExtension<?, ?> living) {
    living.performAction(ActionTypes.REMOVE_MAGAZINE.get().createAction(living, null), true);
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
