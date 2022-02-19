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

public class RemoveMagazineAction extends TimedAction {

  private final LivingExtension<?, ?> performer;

  private final Gun gun;

  private final ItemStack oldMagazineStack;

  private final MagazineAmmoProvider ammoProvider;

  @Nullable
  private Animation animation;

  public RemoveMagazineAction(LivingExtension<?, ?> performer) {
    this.performer = performer;
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
  public ActionType<?> getType() {
    return ActionTypes.REMOVE_MAGAZINE.get();
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.gun.getReloadDurationTicks() / 2;
  }

  @Override
  public boolean start(boolean simulate) {
    if (this.getPerformer().getEntity().isSprinting() || this.oldMagazineStack.isEmpty()) {
      return false;
    }

    if (simulate) {
      return true;
    }

    if (this.gun.isPerformingSecondaryAction()) {
      this.gun.setPerformingSecondaryAction(this.getPerformer(), false, false);
    }

    if (this.getPerformer().getLevel().isClientSide()) {
      this.animation = this.gun.getClient().getAnimation(GunAnimationEvent.RELOAD);
      this.gun.getClient().getAnimationController().addAnimation(this.animation);
    }

    return true;
  }

  @Override
  public boolean tick() {
    if (!this.performer.getLevel().isClientSide()
        && !this.performer.getMainHandItem().is(this.gun.getItemStack().getItem())
        || this.getPerformer().getEntity().isSprinting()) {
      this.getPerformer().cancelAction(true);
      return false;
    }
    return super.tick();
  }

  @Override
  public void stop(StopReason reason) {
    super.stop(reason);

    if (reason.isCompleted()) {
      if (!this.getPerformer().getLevel().isClientSide()) {
        // This will be synced to the client by the gun.
        this.ammoProvider.setMagazineStack(ItemStack.EMPTY);
        if (!this.oldMagazineStack.isEmpty()
            && this.getPerformer().getEntity() instanceof Player player) {
          player.addItem(this.oldMagazineStack);
        }
      }
      return;
    }

    if (this.animation != null) {
      this.animation.remove();
    }
    // Call this on both sides as we change the client side stack for the remove animation.
    this.ammoProvider.setMagazineStack(this.oldMagazineStack);
  }

  @Override
  public LivingExtension<?, ?> getPerformer() {
    return this.performer;
  }
}
