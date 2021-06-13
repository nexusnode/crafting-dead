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
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.client.animation.AnimationType;
import com.craftingdead.core.client.animation.reload.GunAnimationReload;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.Gun;
import com.craftingdead.core.world.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.gun.ammoprovider.MagazineAmmoProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class RemoveMagazineAction extends TimedAction<ActionType> {

  private final Gun gun;

  private final ItemStack oldMagazineStack;

  private final MagazineAmmoProvider ammoProvider;

  public RemoveMagazineAction(ActionType type, LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target) {
    super(type, performer, target);
    this.gun = performer.getEntity().getMainHandItem().getCapability(Capabilities.GUN)
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
        this.gun.getClient().getAnimation(AnimationType.RELOAD)
            .filter(animation -> animation instanceof GunAnimationReload)
            .map(animation -> (GunAnimationReload) animation)
            .ifPresent(animation -> {
              animation.setEjectingClip(true);
              this.gun.getClient().getAnimationController().addAnimation(animation,
                  () -> this.ammoProvider.setMagazineStack(ItemStack.EMPTY));
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
    if (this.getPerformer().getLevel().isClientSide()) {
      this.gun.getClient().getAnimationController().removeCurrentAnimation();
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
          && this.getPerformer().getEntity() instanceof PlayerEntity) {
        ((PlayerEntity) this.getPerformer().getEntity()).addItem(this.oldMagazineStack);
      }
    }
  }
}

