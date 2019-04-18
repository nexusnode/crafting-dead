package com.craftingdead.mod.capability.player;

import java.util.Random;
import java.util.UUID;

import com.craftingdead.mod.capability.triggerable.Triggerable;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.init.ModPotions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

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
	 * Random
	 */
	private static final Random RANDOM = new Random();
	/**
	 * The vanilla entity
	 */
	protected final E entity;
	/**
	 * Days survived
	 */
	protected int daysSurvived;
	/**
	 * Zombies killed
	 */
	protected int zombiesKilled;
	/**
	 * Players killed
	 */
	protected int playersKilled;
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
		if (!this.entity.capabilities.isCreativeMode && !this.entity.isPotionActive(ModPotions.BROKEN_LEG)
				&& this.entity.onGround && !this.entity.isInWater()
				&& ((this.entity.fallDistance > 4F && RANDOM.nextInt(3) == 0) || this.entity.fallDistance > 10F)) {
			this.entity.sendStatusMessage(new TextComponentTranslation("message.broken_leg")
					.setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
			this.entity.addPotionEffect(new PotionEffect(ModPotions.BROKEN_LEG, 9999999, 4));
			this.entity.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 1));
		}
		if (this.entity.capabilities.isCreativeMode) {
			if (this.entity.isPotionActive(ModPotions.BROKEN_LEG)) {
				this.entity.removePotionEffect(ModPotions.BROKEN_LEG);
			}
		}
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

	@Override
	public boolean onKill(Entity target) {
		return false;
	}

	@Override
	public boolean onDeath(DamageSource cause) {
		return false;
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
		nbt.setInteger("zombieKills", this.zombiesKilled);
		nbt.setInteger("playerKills", this.playersKilled);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.daysSurvived = nbt.getInteger("daysSurvived");
		this.zombiesKilled = nbt.getInteger("zombieKills");
		this.playersKilled = nbt.getInteger("playerKills");
	}

	@Override
	public int getDaysSurvived() {
		return this.daysSurvived;
	}

	@Override
	public int getZombiesKilled() {
		return this.zombiesKilled;
	}

	@Override
	public int getPlayersKilled() {
		return this.playersKilled;
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
