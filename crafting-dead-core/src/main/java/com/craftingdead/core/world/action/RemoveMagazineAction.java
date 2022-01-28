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

package com.craftingdead.core.world.action;

import javax.annotation.Nullable;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunAnimationEvent;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.MagazineAmmoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RemoveMagazineAction extends TimedAction<ActionType> {

  private final Gun gun;

  private final ItemStack oldMagazineStack;

  private final MagazineAmmoProvider ammoProvider;

  @Nullable
  private Animation animation;

  public RemoveMagazineAction(ActionType type, LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target) {
    super(type, performer, target);
    this.gun = performer.getMainHandGun()
        .orElseThrow(() -> new IllegalStateException("Performer not holding gun"));
    AmmoProvider ammoProvider = this.gun.getAmmoProvider();
    if (!(ammoProvider instanceof MagazineAmmoProvider)) {
      throw new IllegalStateException("No MagazineAmmoProvider present");
    }
    this.ammoProvider = (MagazineAmmoProvider) ammoProvider;
    this.oldMagazineStack = this.ammoProvider.getMagazineStack();
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.gun.getReloadDurationTicks() / 2;
  }

  @Override
  public boolean start() {
    if (!this.getPerformer().getEntity().isSprinting() && !this.oldMagazineStack.isEmpty()) {
      if (this.gun.isPerformingSecondaryAction()) {
        this.gun.setPerformingSecondaryAction(this.getPerformer(), false, false);
      }
      if (this.getPerformer().getLevel().isClientSide()) {
        this.animation = this.gun.getClient().getAnimation(GunAnimationEvent.RELOAD);
        this.gun.getClient().getAnimationController().addAnimation(this.animation);
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean tick() {
    if (this.getPerformer().getEntity().isSprinting()) {
      this.getPerformer().cancelAction(false);
      return false;
    }
    return super.tick();
  }

  @Override
  public void cancel() {
    super.cancel();
    if (this.animation != null) {
      this.animation.remove();
    }
    // Call this on both sides as we change the client side stack for the remove animation.
    this.ammoProvider.setMagazineStack(this.oldMagazineStack);
  }

  @Override
  protected void finish() {
    if (!this.getPerformer().getLevel().isClientSide()) {
      // This will be synced to the client by the gun.
      this.ammoProvider.setMagazineStack(ItemStack.EMPTY);
      if (!this.oldMagazineStack.isEmpty()
          && this.getPerformer().getEntity() instanceof Player) {
        ((Player) this.getPerformer().getEntity()).addItem(this.oldMagazineStack);
      }
    }
  }
}

