package com.craftingdead.mod.capability.player;

import java.util.Random;
import java.util.UUID;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.potion.ModEffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * The abstracted player class - represents a Crafting Dead player.<br>
 * Subclasses are attached to the appropriate {@link E} via Forge capabilities.
 * 
 * @author Sm0keySa1m0n
 *
 * @param <E> - the associated {@link PlayerEntity}
 */
public class DefaultPlayer<E extends PlayerEntity> implements Player<E> {
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
		if (!this.entity.isCreative() && !this.entity.isPotionActive(ModEffects.BROKEN_LEG) && this.entity.onGround
				&& !this.entity.isInWater()
				&& ((this.entity.fallDistance > 4F && RANDOM.nextInt(3) == 0) || this.entity.fallDistance > 10F)) {
			this.entity.sendStatusMessage(new TranslationTextComponent("message.broken_leg")
					.setStyle(new Style().setColor(TextFormatting.RED).setBold(true)), true);
			this.entity.addPotionEffect(new EffectInstance(ModEffects.BROKEN_LEG, 9999999, 4));
			this.entity.addPotionEffect(new EffectInstance(Effects.field_76440_q, 100, 1));
		}
		if (this.entity.isCreative()) {
			if (this.entity.isPotionActive(ModEffects.BROKEN_LEG)) {
				this.entity.removePotionEffect(ModEffects.BROKEN_LEG);
			}
		}
	}

	private void updateHeldStack() {
		ItemStack heldStack = this.entity.getHeldItemMainhand();
		if (heldStack != this.lastHeldStack) {
			if (this.lastHeldStack != null) {
				this.lastHeldStack.getCapability(ModCapabilities.TRIGGERABLE).ifPresent(
						(triggerable) -> triggerable.setTriggerPressed(false, this.lastHeldStack, this.entity));
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
		heldStack.getCapability(ModCapabilities.TRIGGERABLE, null)
				.ifPresent((triggerable) -> triggerable.setTriggerPressed(triggerPressed, heldStack, this.getEntity()));
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("daysSurvived", this.daysSurvived);
		nbt.putInt("zombiesKilled", this.zombiesKilled);
		nbt.putInt("playersKilled", this.playersKilled);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		this.daysSurvived = nbt.getInt("daysSurvived");
		this.zombiesKilled = nbt.getInt("zombiesKilled");
		this.playersKilled = nbt.getInt("playersKilled");
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
		return this.entity != null ? this.entity.getUniqueID() : null;
	}
}
