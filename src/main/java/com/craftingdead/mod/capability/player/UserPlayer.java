package com.craftingdead.mod.capability.player;

import java.util.Random;

import net.minecraft.client.entity.EntityPlayerSP;

public class UserPlayer extends DefaultPlayer<EntityPlayerSP> {

	private static final Random RANDOM = new Random();

	private static final float MINIMUM_SPREAD = 0.5F, MAXIMUM_SPREAD = 60.0F, MOVEMENT_SPREAD = 12.5F, SPREAD_INCREMENT = 5.0F;

	private float baseSpread = 0.0F;
	private float actualSpread = 0.0F, lastActualSpread = actualSpread;
	private float spread = 0.0F;

	private float recoil;

	public UserPlayer(EntityPlayerSP entity) {
		super(entity);
	}

	public void updateStatistics(int daysSurvived, int zombieKills, int playerKills) {
		this.daysSurvived = daysSurvived;
		this.zombieKills = zombieKills;
		this.playerKills = playerKills;
	}

	@Override
	public void update() {
		super.update();
		this.updateSpread();
		this.applyPendingRecoil();
	}

	private void applyPendingRecoil() {
		if (this.recoil > 0) {
			float pitch = this.entity.rotationPitch, yaw = this.entity.rotationYaw;

			float randomRecoil = this.recoil - RANDOM.nextFloat();
			switch (RANDOM.nextInt(3)) {
			case 0:
				pitch -= randomRecoil;
				break;
			case 1:
				yaw -= randomRecoil;
				break;
			case 2:
				yaw += randomRecoil;
				break;
			}

			pitch -= randomRecoil / 2;

			this.entity.rotationPitch = this.entity.prevRotationPitch + (pitch - this.entity.prevRotationPitch);
			this.entity.rotationYaw = this.entity.prevRotationYaw + (yaw - this.entity.prevRotationYaw);

			this.recoil = 0;
		}
	}

	private void updateSpread() {
		final float originalSpread = this.actualSpread;

		if (this.entity.posX != this.entity.lastTickPosX || this.entity.posY != this.entity.lastTickPosY
				|| this.entity.posZ != this.entity.lastTickPosZ) {
			float movementSpread = MOVEMENT_SPREAD;

			if (this.entity.isSprinting())
				movementSpread *= 2F;

			if (this.entity.isSneaking() && this.entity.onGround)
				movementSpread /= 2F;

			if (this.actualSpread < movementSpread)
				this.actualSpread += SPREAD_INCREMENT;
		}

		// If the spread hasn't changed, decrease it
		if (this.actualSpread == originalSpread) {
			this.actualSpread -= SPREAD_INCREMENT;
		}

		if (this.actualSpread < this.baseSpread) {
			this.actualSpread = this.baseSpread;
		}

		if (this.actualSpread < MINIMUM_SPREAD) {
			this.actualSpread = MINIMUM_SPREAD;
		}

		if (this.actualSpread > MAXIMUM_SPREAD) {
			this.actualSpread = MAXIMUM_SPREAD;
		}

		this.spread = (this.actualSpread + this.lastActualSpread) / 2.0F;
		this.lastActualSpread = this.actualSpread;
	}

	public void setBaseSpread(float baseSpread) {
		this.baseSpread = baseSpread;
	}
	
	public void queueRecoil() {
		this.recoil = (float) Math.log(this.spread * 2);
	}
	
	public float getSpread() {
		return this.spread;
	}

}
