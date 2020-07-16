package com.craftingdead.core.action;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class RemoveMagazineAction extends TimedAction {

  private final IGun gun;

  public RemoveMagazineAction(ILiving<?> performer) {
    super(ActionTypes.REMOVE_MAGAZINE.get(), performer, null);
    this.gun = performer.getEntity().getHeldItemMainhand().getCapability(ModCapabilities.GUN)
        .orElseThrow(() -> new IllegalStateException("Performer not holding gun"));
  }

  @Override
  protected int getTotalDurationTicks() {
    return this.gun.getReloadDurationTicks() / 2;
  }

  @Override
  public boolean start() {
    return !this.gun.getMagazineStack().isEmpty();
  }

  @Override
  protected void finish() {
    ItemStack oldMagazine = this.gun.getMagazineStack();
    this.gun.setMagazineStack(ItemStack.EMPTY);
    if (!oldMagazine.isEmpty() && this.performer.getEntity() instanceof PlayerEntity) {
      ((PlayerEntity) this.performer.getEntity()).addItemStackToInventory(oldMagazine);
    }
  }
}

