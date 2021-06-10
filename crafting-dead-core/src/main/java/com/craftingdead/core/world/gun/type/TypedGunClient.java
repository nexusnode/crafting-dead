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

package com.craftingdead.core.world.gun.type;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.client.animation.AnimationType;
import com.craftingdead.core.client.animation.GunAnimation;
import com.craftingdead.core.world.gun.AbstractGunClient;
import net.minecraft.util.SoundEvent;

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
  public Optional<GunAnimation> getAnimation(AnimationType animationType) {
    return Optional.ofNullable(this.gun.getType().getAnimations().get(animationType))
        .map(Supplier::get);
  }
}
