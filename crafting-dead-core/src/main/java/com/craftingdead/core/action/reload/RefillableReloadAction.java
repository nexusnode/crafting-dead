package com.craftingdead.core.action.reload;

import com.craftingdead.core.action.ActionTypes;
import com.craftingdead.core.ammoprovider.IAmmoProvider;
import com.craftingdead.core.ammoprovider.RefillableAmmoProvider;
import com.craftingdead.core.capability.living.ILiving;

public class RefillableReloadAction extends AbstractReloadAction {

  private final RefillableAmmoProvider ammoProvider;

  private int oldAmmoCount;

  public RefillableReloadAction(ILiving<?, ?> performer) {
    super(ActionTypes.REFILLABLE_RELOAD.get(), performer);
    IAmmoProvider ammoProvider = this.gun.getAmmoProvider();
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
