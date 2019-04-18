package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.EntityCorpse;
import com.craftingdead.mod.network.message.server.SetTriggerPressedSMessage;
import com.craftingdead.mod.network.message.server.UpdateStatisticsSMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ServerPlayer extends DefaultPlayer<EntityPlayerMP> {
	/**
	 * Used to calculate if a day has passed by
	 */
	private boolean lastDay;
	/**
	 * Used to detect if {@link #daysSurvived} has changed since it was last sent to
	 * the client
	 */
	private int lastDaysSurvived = Integer.MIN_VALUE;
	/**
	 * Used to detect if {@link #zombiesKilled} has changed since it was last sent
	 * to the client
	 */
	private int lastZombieKills = Integer.MIN_VALUE;
	/**
	 * Used to detect if {@link #playersKilled} has changed since it was last sent
	 * to the client
	 */
	private int lastPlayerKills = Integer.MIN_VALUE;

	public ServerPlayer(EntityPlayerMP entity) {
		super(entity);
		this.lastDay = entity.getEntityWorld().isDaytime();
	}

	/**
	 * Update the player
	 */
	@Override
	public void update() {
		super.update();
		this.updateDaysSurvived();
		this.updateStatistics();
	}

	private void updateStatistics() {
		if (this.daysSurvived != this.lastDaysSurvived || this.zombiesKilled != this.lastZombieKills
				|| this.playersKilled != this.lastPlayerKills) {
			this.sendMessage(new UpdateStatisticsSMessage(this.daysSurvived, this.zombiesKilled, this.playersKilled));
			this.lastDaysSurvived = this.daysSurvived;
			this.lastZombieKills = this.zombiesKilled;
			this.lastPlayerKills = this.playersKilled;
		}
	}

	private void updateDaysSurvived() {
		boolean isDay = this.entity.getEntityWorld().isDaytime();
		// If it was night time and is now day time then increment their days survived
		// by 1
		if (!this.lastDay && isDay)
			this.daysSurvived++;
		this.lastDay = isDay;
	}

	@Override
	public boolean onKill(Entity target) {
		if (target instanceof EntityZombie) {
			this.zombiesKilled++;
		} else if (target instanceof EntityPlayerMP) {
			this.playersKilled++;
		}
		return false;
	}

	@Override
	public boolean onDeath(DamageSource cause) {
		EntityCorpse corpse = new EntityCorpse(this.entity);
		this.entity.world.spawnEntity(corpse);
		return false;
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed) {
		super.setTriggerPressed(triggerPressed);
		CraftingDead.NETWORK_WRAPPER
				.sendToAllTracking(new SetTriggerPressedSMessage(entity.getEntityId(), triggerPressed), entity);
	}

	public void copyFrom(ServerPlayer that, boolean wasDeath) {
		if (!wasDeath) {
			this.daysSurvived = that.daysSurvived;
			this.zombiesKilled = that.zombiesKilled;
			this.playersKilled = that.playersKilled;
		}
	}

	/**
	 * Send a message to the player's client
	 * 
	 * @param message - the {@link IMessage} to send
	 */
	public void sendMessage(IMessage msg) {
		CraftingDead.NETWORK_WRAPPER.sendTo(msg, this.entity);
	}

}
