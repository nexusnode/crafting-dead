package com.craftingdead.mod.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;

public class EntityAICDZombieAttack extends EntityAIAttackMelee {

	public EntityAICDZombieAttack(EntityCDZombie zombieIn, double speedIn, boolean longMemoryIn) {
		super(zombieIn, speedIn, longMemoryIn);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask() {
		super.updateTask();
		EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
		if (this.attacker.getDistance(entitylivingbase) <= 1.5F) {
			if (this.attackTick <= 0) {
				this.attackTick = 10;
				if (this.attacker.getHeldItemMainhand() != null) {
					this.attacker.swingArm(EnumHand.MAIN_HAND);
				}
				this.attacker.attackEntityAsMob(entitylivingbase);
			}
		}
	}
}
