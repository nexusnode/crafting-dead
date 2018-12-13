package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.network.message.MessageUpdateStatistics;
import com.craftingdead.mod.server.LogicalServer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ServerPlayer extends DefaultPlayer<EntityPlayerMP> {

	private final LogicalServer logicalServer;

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
	 * Used to detect if {@link #zombieKills} has changed since it was last sent to
	 * the client
	 */
	private int lastZombieKills = Integer.MIN_VALUE;
	/**
	 * Used to detect if {@link #playerKills} has changed since it was last sent to
	 * the client
	 */
	private int lastPlayerKills = Integer.MIN_VALUE;


	public ServerPlayer(EntityPlayerMP entity, LogicalServer logicalServer) {
		super(entity);
		this.logicalServer = logicalServer;
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
		if (this.daysSurvived != this.lastDaysSurvived || this.zombieKills != this.lastZombieKills
				|| this.playerKills != this.lastPlayerKills) {
			this.sendMessage(new MessageUpdateStatistics(this.daysSurvived, this.zombieKills, this.playerKills));
			this.lastDaysSurvived = this.daysSurvived;
			this.lastZombieKills = this.zombieKills;
			this.lastPlayerKills = this.playerKills;
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
	public void onKill(Entity target, DamageSource cause) {
		if (target instanceof EntityZombie) {
			this.zombieKills++;
		} else if (target instanceof EntityPlayerMP) {
			this.playerKills++;
		}
	}

	public void copyFrom(ServerPlayer that, boolean wasDeath) {
		if (!wasDeath) {
			this.daysSurvived = that.daysSurvived;
			this.zombieKills = that.zombieKills;
			this.playerKills = that.playerKills;
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

	public LogicalServer getServer() {
		return this.logicalServer;
	}

}
