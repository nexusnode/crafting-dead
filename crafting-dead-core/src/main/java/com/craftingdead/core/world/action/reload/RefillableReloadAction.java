/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.core.world.action.reload;

import javax.annotation.Nullable;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.item.gun.ammoprovider.AmmoProvider;
import com.craftingdead.core.world.item.gun.ammoprovider.RefillableAmmoProvider;

public class RefillableReloadAction extends AbstractReloadAction {

  private final RefillableAmmoProvider ammoProvider;

  private int oldAmmoCount;

  public RefillableReloadAction(ActionType type, LivingExtension<?, ?> performer,
      @Nullable LivingExtension<?, ?> target) {
    super(type, performer, target);
    AmmoProvider ammoProvider = this.gun.getAmmoProvider();
    if (!(ammoProvider instanceof RefillableAmmoProvider)) {
      throw new IllegalStateException("No RefillableAmmoProvider present");
    }
    this.ammoProvider = (RefillableAmmoProvider) ammoProvider;
  }

  @Override
  public boolean start() {
    return (this.ammoProvider.hasInfiniteAmmo() || this.ammoProvider.getReserveSize() > 0)
        && super.start();
  }

  @Override
  protected void loadNewMagazineStack(boolean displayOnly) {
    if (!displayOnly) {
      this.oldAmmoCount = this.ammoProvider.getExpectedMagazine().getSize();
      this.ammoProvider.refillMagazine();
    }
  }

  @Override
  protected void revert() {
    this.ammoProvider.moveAmmoToReserve(this.ammoProvider.getExpectedMagazine().getSize());
    this.ammoProvider.moveAmmoToMagazine(this.oldAmmoCount);
  }
}
