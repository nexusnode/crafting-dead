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
