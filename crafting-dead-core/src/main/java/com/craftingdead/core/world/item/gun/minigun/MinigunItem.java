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

package com.craftingdead.core.world.item.gun.minigun;

import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunConfiguration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class MinigunItem extends GunItem {

  protected MinigunItem(Builder<?> builder) {
    super(builder);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, CompoundTag nbt) {
    return CapabilityUtil.serializableProvider(() -> new Minigun(itemStack, this),
        Gun.CAPABILITY, CombatSlotProvider.CAPABILITY);
  }

  public static Builder<?> builder(ResourceKey<GunConfiguration> configurationKey) {
    return new Builder<>(MinigunItem::new, configurationKey);
  }
}
