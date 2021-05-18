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

package com.craftingdead.core.world.action.reload;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.client.animation.gun.AnimationType;
import com.craftingdead.core.client.animation.gun.reload.GunAnimationReload;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.TimedAction;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.gun.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public abstract class AbstractReloadAction extends TimedAction {

  protected final ItemStack gunStack;

  protected final IGun gun;

  protected final ItemStack oldMagazineStack;

  public AbstractReloadAction(ActionType<?> actionType, LivingExtension<?, ?> performer) {
    super(actionType, performer, null);
    this.gunStack = performer.getEntity().getMainHandItem();
    this.gun = this.gunStack.getCapability(ModCapabilities.GUN)
        .orElseThrow(() -> new IllegalStateException("Performer not holding gun"));
    this.oldMagazineStack = this.gun.getAmmoProvider().getMagazineStack();
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.gun.getReloadDurationTicks();
  }

  @Override
  public boolean start() {
    if (this.getPerformer().getEntity().isSprinting()) {
      return false;
    }

    if (this.gun.isPerformingRightMouseAction()) {
      this.gun.setPerformingRightMouseAction(this.getPerformer(), false, false);
    }

    // Some guns may not have a reload sound
    this.gun.getReloadSound()
        .ifPresent(sound -> this.performer.getEntity().getCommandSenderWorld().playSound(null,
            this.performer.getEntity(), sound, SoundCategory.PLAYERS, 1.0F, 1.0F));

    if (this.performer.getEntity().getCommandSenderWorld().isClientSide()) {
      if (this.oldMagazineStack.isEmpty()) {
        this.playLoadAnimation(false, null);
      } else {
        this.playLoadAnimation(true, () -> this.playLoadAnimation(false, null));
      }
    }

    return true;
  }

  protected abstract void loadNewMagazineStack(boolean displayOnly);

  private void playLoadAnimation(boolean unload, @Nullable Runnable callback) {
    if (!unload) {
      // Load new magazine stack into gun for animation purposes
      this.loadNewMagazineStack(true);
    }
    this.gun.getAnimation(AnimationType.RELOAD)
        .filter(animation -> animation instanceof GunAnimationReload)
        .map(animation -> (GunAnimationReload) animation)
        .ifPresent(animation -> {
          animation.setEjectingClip(unload);
          this.gun.getClient().getAnimationController().addAnimation(animation, callback);
        });
  }

  @Override
  public boolean tick() {
    if (!this.getPerformer().getEntity().getCommandSenderWorld().isClientSide()
        && this.getPerformer().getEntity().isSprinting()) {
      this.getPerformer().cancelAction(true);
      return false;
    }
    return super.tick();
  }

  @Override
  protected void finish() {
    if (this.performer.getEntity().getCommandSenderWorld().isClientSide()) {
      return;
    }
    // This will be synced to the client by the gun.
    this.loadNewMagazineStack(false);
  }

  @Override
  public void cancel() {
    super.cancel();
    if (this.gun.getClient() != null) {
      if (this.gun.getReloadSound().isPresent()) {
        // Stop reload sound
        Minecraft.getInstance().getSoundManager()
            .stop(this.gun.getReloadSound().get().getRegistryName(), SoundCategory.PLAYERS);
      }
      this.gun.getClient().getAnimationController().removeCurrentAnimation();
    }

    // Revert all changes as we've been cancelled
    this.revert();
  }

  protected abstract void revert();
}
