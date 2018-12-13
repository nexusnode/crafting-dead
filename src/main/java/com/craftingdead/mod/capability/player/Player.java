package com.craftingdead.mod.capability.player;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.INBTSerializable;

public interface Player<E extends EntityPlayer> extends INBTSerializable<NBTTagCompound>, ITickable {

	/**
	 * When the player kills another entity
	 * 
	 * @param target - the {@link Entity} killed
	 * @param cause  - the {@link DamageSource}
	 */
	void onKill(Entity target, DamageSource cause);

	void setTriggerPressed(boolean triggerPressed);

	int getDaysSurvived();

	int getZombieKills();

	int getPlayerKills();

	E getEntity();

	UUID getUUID();

}
