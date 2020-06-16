package com.craftingdead.core.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class AdrenalineEffect extends Effect {

  protected AdrenalineEffect() {
    super(EffectType.BENEFICIAL, 0xFFD000);
    this
        .addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED,
            "91AEAA56-376B-4498-935B-2F7F68070635", (double) 0.2F,
            AttributeModifier.Operation.MULTIPLY_TOTAL);
  }

  @Override
  public void removeAttributesModifiersFromEntity(LivingEntity livingEntity,
      AbstractAttributeMap attributes, int amplifier) {
    super.removeAttributesModifiersFromEntity(livingEntity, attributes, amplifier);
    livingEntity
        .setAbsorptionAmount(livingEntity.getAbsorptionAmount() - (float) (4 * (amplifier + 1)));
  }

  @Override
  public void applyAttributesModifiersToEntity(LivingEntity livingEntity,
      AbstractAttributeMap attributes, int amplifier) {
    super.applyAttributesModifiersToEntity(livingEntity, attributes, amplifier);
    livingEntity
        .setAbsorptionAmount(livingEntity.getAbsorptionAmount() + (float) (4 * (amplifier + 1)));
  }
}
