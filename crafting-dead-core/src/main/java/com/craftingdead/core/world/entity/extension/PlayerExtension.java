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

package com.craftingdead.core.world.entity.extension;

import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.inventory.ModEquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface PlayerExtension<E extends Player>
    extends LivingExtension<E, PlayerHandler>, PlayerHandler {

  static <E extends Player> PlayerExtension<E> create(E entity) {
    return new PlayerExtensionImpl<>(entity);
  }

  @SuppressWarnings("unchecked")
  static <P extends Player> PlayerExtension<P> getOrThrow(P player) {
    return CapabilityUtil.getOrThrow(LivingExtension.CAPABILITY, player, PlayerExtension.class);
  }

  @Nullable
  @SuppressWarnings("unchecked")
  static <P extends Player> PlayerExtension<P> get(P player) {
    return CapabilityUtil.get(LivingExtension.CAPABILITY, player, PlayerExtension.class);
  }

  boolean isCombatModeEnabled();

  void setCombatModeEnabled(boolean combatModeEnabled);

  void openEquipmentMenu();

  void openStorage(ModEquipmentSlot slot);

  ItemStack getHandcuffs();

  default boolean isHandcuffed() {
    return !this.getHandcuffs().isEmpty();
  }

  void setHandcuffs(ItemStack itemStack);

  default boolean damageHandcuffs(int damage) {
    final var handcuffs = this.getHandcuffs().copy();
    handcuffs.hurtAndBreak(damage, this.entity(), __ -> this.breakItem(handcuffs));
    if (handcuffs.isEmpty()) {
      this.setHandcuffs(ItemStack.EMPTY);
      return true;
    }
    this.setHandcuffs(handcuffs);
    return false;
  }
}
