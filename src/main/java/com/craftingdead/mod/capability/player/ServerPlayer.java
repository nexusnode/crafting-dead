package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.entity.CorpseEntity;
import com.craftingdead.mod.message.server.SUpdateStatisticsMessage;
import com.craftingdead.mod.message.server.ServerTriggerPressedMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.network.PacketDistributor;

public class ServerPlayer extends DefaultPlayer<ServerPlayerEntity> {
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

	public ServerPlayer(ServerPlayerEntity entity) {
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
			CraftingDead.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(this::getEntity),
					new SUpdateStatisticsMessage(this.daysSurvived, this.zombiesKilled, this.playersKilled));
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
		if (target instanceof ZombieEntity) {
			this.zombiesKilled++;
		} else if (target instanceof ServerPlayerEntity) {
			this.playersKilled++;
		}
		return false;
	}

	@Override
	public boolean onDeath(DamageSource cause) {
		CorpseEntity corpse = new CorpseEntity(this.entity);
		this.entity.world.func_217376_c(corpse);
		return false;
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed) {
		super.setTriggerPressed(triggerPressed);
		CraftingDead.NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(this::getEntity),
				new ServerTriggerPressedMessage(this.entity.getEntityId(), triggerPressed));
	}

	public void copyFrom(ServerPlayer that, boolean wasDeath) {
		if (!wasDeath) {
			this.daysSurvived = that.daysSurvived;
			this.zombiesKilled = that.zombiesKilled;
			this.playersKilled = that.playersKilled;
		}
	}
}
