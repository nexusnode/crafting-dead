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

	public PlayerSP(EntityPlayerSP vanillaEntity) {
		this.entity = vanillaEntity;
	}

	public void setDaysSurvived(int daysSurvived) {
		this.daysSurvived = daysSurvived;
	}

	public int getDaysSurvived() {
		return this.daysSurvived;
	}

	public EntityPlayerSP getVanillaEntity() {
		return this.entity;
	}

}
