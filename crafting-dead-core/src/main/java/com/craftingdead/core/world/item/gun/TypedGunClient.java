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

import java.util.Optional;
import com.craftingdead.core.client.animation.Animation;
import net.minecraft.sounds.SoundEvent;

public class TypedGunClient<T extends TypedGun<?>> extends AbstractGunClient<T> {

  public TypedGunClient(T gun) {
    super(gun);
  }

  @Override
  protected Optional<SoundEvent> getSecondaryActionSound() {
    return this.gun.getType().getSecondaryActionSound();
  }

  @Override
  protected long getSecondaryActionSoundRepeatDelayMs() {
    return this.gun.getType().getSecondaryActionSoundRepeatDelayMs();
  }

  @Override
  protected float getRecoil() {
    return this.gun.getType().getRecoil();
  }

  @Override
  protected SoundEvent getShootSound() {
    return this.gun.getType().getShootSound();
  }

  @Override
  protected Optional<SoundEvent> getDistantShootSound() {
    return this.gun.getType().getDistantShootSound();
  }

  @Override
  protected Optional<SoundEvent> getSilencedShootSound() {
    return this.gun.getType().getSilencedShootSound();
  }

  @Override
  public boolean hasCrosshair() {
    return this.gun.getType().hasCrosshair();
  }

  @Override
  public Animation getAnimation(GunAnimationEvent event) {
    return this.gun.getType().getAnimations().get(event).apply(this.gun.getType());
  }
}
