package com.craftingdead.core.action;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.client.renderer.item.gun.AnimationType;
import com.craftingdead.core.client.renderer.item.gun.GunAnimation;
import com.craftingdead.core.client.renderer.item.gun.reload.GunAnimationReload;
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
      if (this.performer.getEntity().getEntityWorld().isRemote()) {
        GunAnimation ejectAnimation = this.gun.getAnimation(AnimationType.RELOAD);
        if (ejectAnimation instanceof GunAnimationReload) {
          ((GunAnimationReload) ejectAnimation).setEjectingClip(true);
          this.gun.getItemRenderer().addAnimation(ejectAnimation,
              () -> this.gun.setMagazineStack(ItemStack.EMPTY));
        }
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
      this.gun.getItemRenderer().removeCurrentAnimation();
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

