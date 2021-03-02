package com.craftingdead.core.action.reload;

import javax.annotation.Nullable;
import com.craftingdead.core.action.ActionType;
import com.craftingdead.core.action.TimedAction;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.animationprovider.gun.AnimationType;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimationController;
import com.craftingdead.core.capability.animationprovider.gun.reload.GunAnimationReload;
import com.craftingdead.core.capability.gun.IGun;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public abstract class AbstractReloadAction extends TimedAction {

  protected final ItemStack gunStack;

  protected final IGun gun;

  protected final ItemStack oldMagazineStack;

  public AbstractReloadAction(ActionType<?> actionType, ILiving<?, ?> performer) {
    super(actionType, performer, null);
    this.gunStack = performer.getEntity().getHeldItemMainhand();
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
        .ifPresent(sound -> this.performer.getEntity().getEntityWorld().playMovingSound(null,
            this.performer.getEntity(), sound, SoundCategory.PLAYERS, 1.0F, 1.0F));

    if (this.performer.getEntity().getEntityWorld().isRemote()) {
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
          this.gun.getClient()
              .getAnimationController().ifPresent(c -> c.addAnimation(animation, callback));
        });
  }

  @Override
  public boolean tick() {
    if (!this.getPerformer().getEntity().getEntityWorld().isRemote()
        && this.getPerformer().getEntity().isSprinting()) {
      this.getPerformer().cancelAction(true);
      return false;
    }
    return super.tick();
  }

  @Override
  protected void finish() {
    if (this.performer.getEntity().getEntityWorld().isRemote()) {
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
        Minecraft.getInstance().getSoundHandler()
            .stop(this.gun.getReloadSound().get().getRegistryName(), SoundCategory.PLAYERS);
      }
      this.gun.getClient().getAnimationController()
          .ifPresent(GunAnimationController::removeCurrentAnimation);
    }

    // Revert all changes as we've been cancelled
    this.revert();
  }

  protected abstract void revert();
}
