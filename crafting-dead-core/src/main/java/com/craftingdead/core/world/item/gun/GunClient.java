/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
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

import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.client.animation.AnimationController;

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
   * Get the animation associated with the specified {@link GunAnimationEvent}.
   * 
   * @param event - the {@link GunAnimationEvent} to retrieve an animation for
   * @return the {@link Animation}
   */
  Animation getAnimation(GunAnimationEvent event);

  /**
   * Get the {@link AnimationController} associated with this gun.
   * 
   * @return the {@link AnimationController}
   */
  AnimationController getAnimationController();
}
