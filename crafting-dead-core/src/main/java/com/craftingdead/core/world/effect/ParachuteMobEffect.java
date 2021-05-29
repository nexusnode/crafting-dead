package com.craftingdead.core.world.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.ForgeMod;

public class ParachuteMobEffect extends Effect {

  public ParachuteMobEffect() {
    super(EffectType.BENEFICIAL, 0xFFEFD1);
    this.addAttributeModifier(ForgeMod.ENTITY_GRAVITY.get(), "c5a9e5c2-bd74-11eb-8529-0242ac130003",
        -0.07, AttributeModifier.Operation.ADDITION);
  }

  @Override
  public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
    if (livingEntity.isOnGround()) {
      livingEntity.removeEffect(ModMobEffects.PARACHUTE.get());
      return;
    }
    super.applyEffectTick(livingEntity, amplifier);
  }

  @Override
  public boolean isDurationEffectTick(int duration, int amplifier) {
    return true;
  }

  @Override
  public boolean isInstantenous() {
    return false;
  }
}
