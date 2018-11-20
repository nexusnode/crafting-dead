package com.craftingdead.mod.capability.player;

import java.util.UUID;

import com.craftingdead.mod.capability.SimpleCapability;
import com.craftingdead.mod.capability.triggerable.Triggerable;
import com.craftingdead.mod.init.ModCapabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public abstract class Player<E extends EntityPlayer> implements SimpleCapability, ITickable {

	/**
	 * The vanilla entity
	 */
	private final E entity;
	/**
	 * Days survived
	 */
	protected int daysSurvived;
	/**
	 * Zombie kills
	 */
	protected int zombieKills;
	/**
	 * Player kills
	 */
	protected int playerKills;
	/**
	 * If the trigger on the current held item is pressed
	 */
	protected boolean triggerPressed;

	public Player(E entity) {
		this.entity = entity;
	}

	@Override
	public void update() {
		;
	}

	/**
	 * When the player kills another entity
	 * 
	 * @param target - the {@link Entity} killed
	 * @param cause  - the {@link DamageSource}
	 */
	public void onKill(Entity target, DamageSource cause) {
		;
	}

	public void setTriggerPressed(boolean triggerPressed) {
		this.triggerPressed = triggerPressed;
		ItemStack heldStack = this.getEntity().getHeldItemMainhand();
		Triggerable triggerable = heldStack.getCapability(ModCapabilities.TRIGGERABLE, null);
		if (triggerable != null)
			triggerable.setTriggerPressed(triggerPressed, heldStack, this.getEntity());
	}

	@Override
	public NBTTagCompound writeNBT(EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("daysSurvived", this.daysSurvived);
		nbt.setInteger("zombieKills", this.zombieKills);
		nbt.setInteger("playerKills", this.playerKills);
		return nbt;
	}

	@Override
	public void readNBT(EnumFacing side, NBTTagCompound nbt) {
		this.daysSurvived = nbt.getInteger("daysSurvived");
		this.zombieKills = nbt.getInteger("zombieKills");
		this.playerKills = nbt.getInteger("playerKills");
	}

	public int getDaysSurvived() {
		return this.daysSurvived;
	}

	public int getZombieKills() {
		return this.zombieKills;
	}

	public int getPlayerKills() {
		return this.playerKills;
	}

	public E getEntity() {
		return this.entity;
	}

	public UUID getUUID() {
		return this.entity != null ? this.entity.getPersistentID() : null;
	}

}
