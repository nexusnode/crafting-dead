package com.craftingdead.mod.capability.player;

import net.minecraft.client.entity.AbstractClientPlayer;

public class ClientPlayer extends DefaultPlayer<AbstractClientPlayer> {

	public ClientPlayer(AbstractClientPlayer entity) {
		super(entity);
	}

	public void updateStatistics(int daysSurvived, int zombieKills, int playerKills) {
		this.daysSurvived = daysSurvived;
		this.zombieKills = zombieKills;
		this.playerKills = playerKills;
	}

}
