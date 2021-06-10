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

package com.craftingdead.core.world.gun;

import java.util.Optional;
import com.craftingdead.core.client.animation.AnimationType;
import com.craftingdead.core.client.animation.GunAnimation;
import com.craftingdead.core.client.animation.GunAnimationController;

public interface GunClient {

  /**
   * If a crosshair should be rendered on the HUD for this gun.
   * 
   * @return <code>true</code> if it should be rendered, otherwise <code>false</code>
   */
  boolean hasCrosshair();

  /**
   * If the barrel of the gun is currently 'flashing'.
   * 
   * @return <code>true</code> if flashing, otherwise <code>false</code>
   */
  boolean isFlashing();

  /**
   * Get the animation associated with the specified {@link AnimationType}.
   * 
   * @param animationType - the {@link AnimationType} to retrieve an animation for
   * @return an optional {@link GunAnimation}
   */
  Optional<GunAnimation> getAnimation(AnimationType animationType);

  /**
   * Get the {@link GunAnimationController} associated with this gun.
   * 
   * @return the {@link GunAnimationController}
   */
  GunAnimationController getAnimationController();
}
