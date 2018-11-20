package com.craftingdead.mod.capability.player;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.network.message.MessageSetTriggerPressed;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClientPlayerLocal extends ClientPlayer<EntityPlayerSP> {

	public ClientPlayerLocal(EntityPlayerSP entity) {
		super(entity);
	}

	public void updateStatistics(int daysSurvived, int zombieKills, int playerKills) {
		this.daysSurvived = daysSurvived;
		this.zombieKills = zombieKills;
		this.playerKills = playerKills;
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed) {
		super.setTriggerPressed(triggerPressed);
		this.sendMessage(new MessageSetTriggerPressed(triggerPressed));
	}

	public void sendMessage(IMessage msg) {
		CraftingDead.NETWORK_WRAPPER.sendToServer(msg);
	}

}
