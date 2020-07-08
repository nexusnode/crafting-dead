package com.craftingdead.core.action;

import javax.annotation.Nullable;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.util.Hand;

public class UseItemAction extends AbstractAction {

  public UseItemAction(ActionType<?> actionType, ILiving<?> performer,
      @Nullable ILiving<?> target) {
    super(actionType, performer, target);
  }

  @Override
  public boolean start() {
    this.performer.getEntity().setActiveHand(Hand.MAIN_HAND);
    return this.performer.getEntity().isHandActive();
  }

  @Override
  public boolean tick() {
    if (this.performer.getEntity().getItemInUseCount() == 1) {
      return true;
    }
    if (!this.performer.getEntity().isHandActive()) {
      this.performer.cancelAction(false);
      return false;
    }
    return false;
  }

  @Override
  public void cancel() {
    this.performer.getEntity().stopActiveHand();
  }

  @Override
  public float getProgress(float partialTicks) {
    return (float) (this.performer.getEntity().getItemInUseMaxCount() + partialTicks)
        / this.performer.getEntity().getActiveItemStack().getUseDuration();
  }
}
