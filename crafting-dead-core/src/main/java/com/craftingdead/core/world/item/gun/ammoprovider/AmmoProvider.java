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

import com.craftingdead.core.network.Synched;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface AmmoProvider extends INBTSerializable<CompoundTag>, Synched {

  void reload(LivingExtension<?, ?> living);

  void unload(LivingExtension<?, ?> living);

  int getReserveSize();

  ItemStack getMagazineStack();

  default Magazine getExpectedMagazine() {
    return this.getMagazine()
        .orElseThrow(() -> new IllegalStateException("No magazine capability"));
  }

  default LazyOptional<Magazine> getMagazine() {
    return this.getMagazineStack().getCapability(Magazine.CAPABILITY);
  }

  AmmoProviderType getType();
}
