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

package com.craftingdead.core.world.entity.extension;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public interface PlayerHandler extends LivingHandler {

  default boolean handleAttack(Entity target) {
    return false;
  }

  default boolean handleInteract(InteractionHand hand, Entity target) {
    return false;
  }

  default boolean handleLeftClickBlock(BlockPos pos, Direction face,
      Consumer<Event.Result> attackResult, Consumer<Event.Result> mineResult) {
    return false;
  }

  default boolean handleRightClickBlock(InteractionHand hand, BlockPos pos, Direction face) {
    return false;
  }

  default boolean handleRightClickItem(InteractionHand hand) {
    return false;
  }

  default boolean isCombatModeEnabled() {
    return false;
  }

  default void playerTick() {}

  /**
   * Copy data from old player before death/respawn.<br>
   * <i><b>Only called on server.</b></i>
   * 
   * @param that - the old player
   * @param wasDeath if they died or not
   */
  default void copyFrom(PlayerExtension<?> that, boolean wasDeath) {}
}
