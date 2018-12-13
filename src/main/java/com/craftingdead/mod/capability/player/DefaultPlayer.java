package com.craftingdead.mod.capability.player;

import java.util.UUID;

import com.craftingdead.mod.capability.triggerable.Triggerable;
import com.craftingdead.mod.init.ModCapabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

/**
 * The abstracted player class - represents a Crafting Dead player.<br>
 * Subclasses are attached to the appropriate {@link E} via Forge capabilities.
 * 
 * @author Sm0keySa1m0n
 *
 * @param <E> - the associated {@link EntityPlayer}
 */
public class DefaultPlayer<E extends EntityPlayer> implements Player<E> {

	/**
	 * The vanilla entity
	 */
	protected final E entity;
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
	/**
	 * The last held {@link ItemStack} - used to check if the player has switched
	 * item
	 */
	private ItemStack lastHeldStack = null;

	public DefaultPlayer() {
		this(null);
	}

	public DefaultPlayer(E entity) {
		this.entity = entity;
	}

	@Override
	public void update() {
		this.updateHeldStack();
	}

	private void updateHeldStack() {
		ItemStack heldStack = this.entity.getHeldItemMainhand();
		if (heldStack != this.lastHeldStack) {
			if (this.lastHeldStack != null) {
				Triggerable triggerable = this.lastHeldStack.getCapability(ModCapabilities.TRIGGERABLE, null);
				if (triggerable != null)
					triggerable.setTriggerPressed(false, this.lastHeldStack, this.entity);
			}
			this.lastHeldStack = heldStack;
		}
	}

	/**
	 * When the player kills another entity
	 * 
	 * @param target - the {@link Entity} killed
	 * @param cause  - the {@link DamageSource}
	 */
	@Override
	public void onKill(Entity target, DamageSource cause) {
		;
	}

	@Override
	public void setTriggerPressed(boolean triggerPressed) {
		this.triggerPressed = triggerPressed;
		ItemStack heldStack = this.getEntity().getHeldItemMainhand();
		Triggerable triggerable = heldStack.getCapability(ModCapabilities.TRIGGERABLE, null);
		if (triggerable != null)
			triggerable.setTriggerPressed(triggerPressed, heldStack, this.getEntity());
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("daysSurvived", this.daysSurvived);
		nbt.setInteger("zombieKills", this.zombieKills);
		nbt.setInteger("playerKills", this.playerKills);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.daysSurvived = nbt.getInteger("daysSurvived");
		this.zombieKills = nbt.getInteger("zombieKills");
		this.playerKills = nbt.getInteger("playerKills");
	}

	@Override
	public int getDaysSurvived() {
		return this.daysSurvived;
	}

	@Override
	public int getZombieKills() {
		return this.zombieKills;
	}

	@Override
	public int getPlayerKills() {
		return this.playerKills;
	}

	@Override
	public E getEntity() {
		return this.entity;
	}

	@Override
	public UUID getUUID() {
		return this.entity != null ? this.entity.getPersistentID() : null;
	}

}
