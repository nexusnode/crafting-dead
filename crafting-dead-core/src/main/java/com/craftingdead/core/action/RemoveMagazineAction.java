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

package com.craftingdead.core.action;

import com.craftingdead.core.ammoprovider.IAmmoProvider;
import com.craftingdead.core.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimationController;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReload;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class RemoveMagazineAction extends TimedAction {

  private final IGun gun;

  private final ItemStack oldMagazineStack;

  private final MagazineAmmoProvider ammoProvider;

  public RemoveMagazineAction(ILiving<?, ?> performer) {
    super(ActionTypes.REMOVE_MAGAZINE.get(), performer, null);
    this.gun = performer.getEntity().getMainHandItem().getCapability(ModCapabilities.GUN)
        .orElseThrow(() -> new IllegalStateException("Performer not holding gun"));
    IAmmoProvider ammoProvider = this.gun.getAmmoProvider();
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
      if (this.gun.isPerformingRightMouseAction()) {
        this.gun.setPerformingRightMouseAction(this.getPerformer(), false, false);
      }
      if (this.performer.getEntity().getCommandSenderWorld().isClientSide()) {
        this.gun.getAnimation(AnimationType.RELOAD)
            .filter(animation -> animation instanceof GunAnimationReload)
            .map(animation -> (GunAnimationReload) animation)
            .ifPresent(animation -> {
              animation.setEjectingClip(true);
              this.gun.getClient().getAnimationController().ifPresent(
                  c -> c.addAnimation(animation,
                      () -> this.ammoProvider.setMagazineStack(ItemStack.EMPTY)));
            });
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
    if (this.gun.getClient() != null) {
      this.gun.getClient().getAnimationController()
          .ifPresent(GunAnimationController::removeCurrentAnimation);
    }
    // Call this on both sides as we change the client side stack for the remove animation.
    this.ammoProvider.setMagazineStack(this.oldMagazineStack);
  }

  @Override
  protected void finish() {
    if (!this.performer.getEntity().getCommandSenderWorld().isClientSide()) {
      // This will be synced to the client by the gun.
      this.ammoProvider.setMagazineStack(ItemStack.EMPTY);
      if (!this.oldMagazineStack.isEmpty() && this.performer.getEntity() instanceof PlayerEntity) {
        ((PlayerEntity) this.performer.getEntity()).addItem(this.oldMagazineStack);
      }
    }
  }
}

