/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReload;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.scope.IScope;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class RemoveMagazineAction extends TimedAction {

  private final IGun gun;

  private final ItemStack oldMagazineStack;

  public RemoveMagazineAction(ILiving<?> performer) {
    super(ActionTypes.REMOVE_MAGAZINE.get(), performer, null);
    this.gun = performer.getEntity().getHeldItemMainhand().getCapability(ModCapabilities.GUN)
        .orElseThrow(() -> new IllegalStateException("Performer not holding gun"));
    this.oldMagazineStack = this.gun.getMagazineStack();
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.gun.getReloadDurationTicks() / 2;
  }

  @Override
  public boolean start() {
    if (!this.getPerformer().getEntity().isSprinting() && !this.gun.getMagazineStack().isEmpty()) {
      if (this.gun instanceof IScope
          && ((IScope) this.gun).isAiming(this.getPerformer().getEntity(),
              this.performer.getEntity().getHeldItemMainhand())) {
        this.gun.toggleRightMouseAction(this.getPerformer(), false);
      }
      if (this.performer.getEntity().getEntityWorld().isRemote()) {
        this.gun.getAnimation(AnimationType.RELOAD)
            .filter(animation -> animation instanceof GunAnimationReload)
            .map(animation -> (GunAnimationReload) animation)
            .ifPresent(animation -> {
              animation.setEjectingClip(true);
              this.gun.getAnimationController().addAnimation(animation,
                  () -> this.gun.setMagazineStack(ItemStack.EMPTY));
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
    if (this.getPerformer().getEntity().getEntityWorld().isRemote()) {
      this.gun.getAnimationController().removeCurrentAnimation();
    }
    this.gun.setMagazineStack(this.oldMagazineStack);
  }

  @Override
  protected void finish() {
    this.gun.setMagazineStack(ItemStack.EMPTY);
    if (!this.oldMagazineStack.isEmpty() && this.performer.getEntity() instanceof PlayerEntity) {
      ((PlayerEntity) this.performer.getEntity()).addItemStackToInventory(this.oldMagazineStack);
    }
  }
}

