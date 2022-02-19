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
