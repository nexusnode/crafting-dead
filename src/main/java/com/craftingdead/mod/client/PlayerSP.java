package com.craftingdead.mod.client;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Wraps around the vanilla {@link EntityPlayerMP} so we can associate our own
 * data, functions and logic. This class acts more of a dummy receiving data
 * from {@link com.craftingdead.mod.common.server.PlayerMP} via packets
 * 
 * @author Sm0keySa1m0n
 *
 */
public class PlayerSP {

	/**
	 * The vanilla entity
	 */
	private final EntityPlayerSP entity;
	/**
	 * Days survived
	 */
	private int daysSurvived;
	/**
	 * Zombie kills
	 */
	private int zombieKills;
	/**
	 * Player kills
	 */
	private int playerKills;

	public PlayerSP(EntityPlayerSP vanillaEntity) {
		this.entity = vanillaEntity;
	}

	public int getDaysSurvived() {
		return this.daysSurvived;
	}

	public void setDaysSurvived(int daysSurvived) {
		this.daysSurvived = daysSurvived;
	}

	public int getZombieKills() {
		return zombieKills;
	}

	public void setZombieKills(int zombieKills) {
		this.zombieKills = zombieKills;
	}

	public int getPlayerKills() {
		return playerKills;
	}

	public void setPlayerKills(int playerKills) {
		this.playerKills = playerKills;
	}

	public EntityPlayerSP getVanillaEntity() {
		return this.entity;
	}

}
