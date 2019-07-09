package com.craftingdead.mod.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BrokenLegEffect extends Effect {

  public BrokenLegEffect() {
    super(EffectType.HARMFUL, 5926017);
    this.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
        "021BEAA1-498F-4D7B-933E-F0FA0B88B9D1", (double) -0.15F,
        AttributeModifier.Operation.MULTIPLY_TOTAL);
  }
}
