package com.craftingdead.core.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.potion.EffectInstance;

public class UseItemEffectAction extends UseItemAction {

  private static final Random random = new Random();

  private final List<Pair<EffectInstance, Float>> effects;
  private final boolean freezeMovement;
  private final boolean applicableToOthers;

  public UseItemEffectAction(ActionType<?> actionType, ILiving<?> performer,
      @Nullable ILiving<?> target) {
    this(actionType, performer, target, new Properties());
  }

  public UseItemEffectAction(ActionType<?> actionType, ILiving<?> performer, ILiving<?> target,
      Properties properties) {
    super(actionType, performer, target);
    this.effects = properties.effects;
    this.freezeMovement = properties.freezeMovement;
    this.applicableToOthers = properties.applicableToOthers;
  }

  @Override
  public boolean tick() {
    boolean finished = super.tick();
    if (this.target != null) {
      if (!this.performer.getEntity().getEntityWorld().isRemote()
          && !this.performer.getEntity().canEntityBeSeen(this.target.getEntity())) {
        this.getPerformer().cancelAction(true);
        return false;
      }
      this.target.setFreezeMovement(this.freezeMovement);
    }
    this.performer.setFreezeMovement(this.freezeMovement);
    if (!this.performer.getEntity().getEntityWorld().isRemote() && finished) {
      ILiving<?> living =
          this.applicableToOthers && this.target != null ? this.target : this.performer;
      for (Pair<EffectInstance, Float> pair : this.effects) {
        if (pair.getLeft() != null && random.nextFloat() < pair.getRight()) {
          EffectInstance effectInstance = pair.getLeft();
          if (effectInstance.getPotion().isInstant()) {
            effectInstance.getPotion().affectEntity(living.getEntity(), living.getEntity(),
                living.getEntity(), effectInstance.getAmplifier(), 1.0D);
          } else {
            living.getEntity().addPotionEffect(new EffectInstance(effectInstance));
          }
        }
      }
    }
    return finished;
  }

  public static class Properties {

    private final List<Pair<EffectInstance, Float>> effects = new ArrayList<>();
    private boolean freezeMovement;
    private boolean applicableToOthers;

    public Properties addEffect(Pair<EffectInstance, Float> effect) {
      this.effects.add(effect);
      return this;
    }

    public Properties setFreezeMovement(boolean freezeMovement) {
      this.freezeMovement = freezeMovement;
      return this;
    }

    public Properties setApplicableToOthers(boolean applicableToOthers) {
      this.applicableToOthers = applicableToOthers;
      return this;
    }
  }
}
