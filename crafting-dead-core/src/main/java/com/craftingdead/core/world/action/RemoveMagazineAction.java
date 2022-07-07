/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.action;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.animation.Animation;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunAnimationEvent;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.MagazineAmmoProvider;
import com.craftingdead.core.world.item.gun.magazine.Magazine;
import org.jetbrains.annotations.Nullable;
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
    this.gun = performer.mainHandGun()
        .orElseThrow(() -> new IllegalStateException("Performer not holding gun"));
    AmmoProvider ammoProvider = this.gun.getAmmoProvider();
    if (!(ammoProvider instanceof MagazineAmmoProvider)) {
      throw new IllegalStateException("No MagazineAmmoProvider present");
    }
    this.ammoProvider = (MagazineAmmoProvider) ammoProvider;
    this.oldMagazineStack = this.ammoProvider.getMagazineStack();
  }

  @Override
  public ActionType<?> type() {
    return ActionTypes.REMOVE_MAGAZINE.get();
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.gun.getReloadDurationTicks() / 2;
  }

  @Override
  public boolean start(boolean simulate) {
    if (this.performer().entity().isSprinting() || this.oldMagazineStack.isEmpty()) {
      return false;
    }

    if (simulate) {
      return true;
    }

    if (this.gun.isPerformingSecondaryAction()) {
      this.gun.setPerformingSecondaryAction(this.performer(), false, false);
    }

    if (this.performer().level().isClientSide()) {
      this.animation = this.gun.getClient().getAnimation(GunAnimationEvent.RELOAD);
      this.gun.getClient().getAnimationController().addAnimation(this.animation);
    }

    return true;
  }

  @Override
  public boolean tick() {
    if (!this.performer.level().isClientSide()
        && !this.performer.mainHandItem().is(this.gun.getItemStack().getItem())
        || this.performer().entity().isSprinting()) {
      this.performer().cancelAction(true);
      return false;
    }
    return super.tick();
  }

  @Override
  public void stop(StopReason reason) {
    super.stop(reason);

    if (reason.isCompleted()) {
      if (!this.performer().level().isClientSide()) {
        // This will be synced to the client by the gun.
        this.ammoProvider.setMagazineStack(ItemStack.EMPTY);
        if (!this.oldMagazineStack.isEmpty()
            && this.performer().entity() instanceof Player
            && !(this.oldMagazineStack.getCapability(Magazine.CAPABILITY).map(Magazine::isEmpty).orElse(true)
            && CraftingDead.serverConfig.reloadDestroyMagWhenEmpty.get())) {
          ((Player) this.performer().entity()).addItem(this.oldMagazineStack);
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
  public LivingExtension<?, ?> performer() {
    return this.performer;
  }
}
