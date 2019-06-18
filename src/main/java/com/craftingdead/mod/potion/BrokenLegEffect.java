package com.craftingdead.mod.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class BrokenLegEffect extends Effect {

	public BrokenLegEffect() {
		super(EffectType.HARMFUL, 5926017);
		this.func_220304_a(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890",
				(double) -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
