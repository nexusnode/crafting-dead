package com.craftingdead.mod.common.multiplayer;

import java.util.UUID;
import java.util.function.Supplier;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.item.ExtendedItem;
import com.craftingdead.mod.common.item.trigger.TriggerHandler;
import com.craftingdead.mod.common.multiplayer.message.MessageUpdateStatistics;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Wraps around the vanilla {@link EntityPlayerMP} so we can associate our own
 * data, functions and logic. This class performs most of the logic associated
 * with the player and forwards data to the client's
 * {@link com.craftingdead.mod.client.multiplayer.PlayerSP} via messages
 * 
 * @author Sm0keySa1m0n
 *
 */
public class PlayerMP implements INBTSerializable<NBTTagCompound>, ITickable {

	/**
	 * The vanilla entity
	 */
	private final EntityPlayerMP entity;
	/**
	 * The server instance used by this session
	 */
	private LogicalServer server;
	/**
	 * Used to calculate if a day has passed by
	 */
	private boolean lastDay;
	/**
	 * Used to calculate {@link #daysSurvived}
	 */
	private int timeAlive;
	/**
	 * Days survived
	 */
	private int daysSurvived;
	/**
	 * Used to detect if {@link #daysSurvived} has changed since it was last sent to
	 * the client
	 */
	private int lastDaysSurvived = Integer.MIN_VALUE;
	/**
	 * Zombie kills
	 */
	private int zombieKills;
	/**
	 * Used to detect if {@link #zombieKills} has changed since it was last sent to
	 * the client
	 */
	private int lastZombieKills = Integer.MIN_VALUE;
	/**
	 * Player kills
	 */
	private int playerKills;
	/**
	 * Used to detect if {@link #playerKills} has changed since it was last sent to
	 * the client
	 */
	private int lastPlayerKills = Integer.MIN_VALUE;

	private TriggerHandler triggerHandler;

	public PlayerMP(LogicalServer server, EntityPlayerMP entity) {
		this.server = server;
		this.entity = entity;
	}

	/**
	 * Serialises the class to a {@link NBTTagCompound} so it can be saved to
	 * {@link EntityPlayerMP#getEntityData()}
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("timeAlive", timeAlive);
		nbt.setInteger("daysSurvived", daysSurvived);
		nbt.setInteger("zombieKills", zombieKills);
		nbt.setInteger("playerKills", playerKills);
		return nbt;
	}

	/**
	 * Deserialises the class from an {@link NBTTagCompound} which is usually read
	 * from {@link EntityPlayerMP#getEntityData()}
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.timeAlive = nbt.getInteger("timeAlive");
		this.daysSurvived = nbt.getInteger("daysSurvived");
		this.zombieKills = nbt.getInteger("zombieKills");
		this.playerKills = nbt.getInteger("playerKills");
	}

	/**
	 * Update the player
	 */
	@Override
	public void update() {
		this.updateDaysSurvived();
		this.updateStatistics();
		if (this.triggerHandler != null)
			this.triggerHandler.update();
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
		boolean isDay = entity.getEntityWorld().isDaytime();
		// If it was night time and is now day time then increment their days survived
		// by 1
		if (!lastDay && isDay)
			daysSurvived++;
		lastDay = isDay;
	}

	public void incrementZombieKills() {
		this.zombieKills++;
	}

	public void incrementPlayerKills() {
		this.playerKills++;
	}

	public void handleTriggerStatusUpdate(boolean triggerDown) {
		// Never trust the client, they may not actually be holding a triggerable item
		ItemStack heldStack = this.entity.getHeldItemMainhand();
		if (heldStack.getItem() instanceof ExtendedItem) {
			ExtendedItem item = (ExtendedItem) heldStack.getItem();
			Supplier<? extends TriggerHandler> triggerHandlerSupplier = item.getTriggerHandlerSupplier();
			if (triggerHandlerSupplier != null)
				if (triggerDown) {
					this.triggerHandler = triggerHandlerSupplier.get();
					this.triggerHandler.triggerDown(this, heldStack);
				} else {
					this.triggerHandler.triggerUp();
					this.triggerHandler = null;
				}
		}
	}

	/**
	 * Called when the player's health reaches 0
	 * 
	 * @param cause - the cause of death
	 */
	public void onDeath(DamageSource cause) {
		this.timeAlive = 0;
		this.daysSurvived = 0;
		this.zombieKills = 0;
		this.playerKills = 0;
	}

	/**
	 * Send a message to the player's client
	 * 
	 * @param message - the {@link IMessage} to send
	 */
	public void sendMessage(IMessage msg) {
		CraftingDead.NETWORK_WRAPPER.sendTo(msg, this.entity);
	}

	public UUID getUUID() {
		return this.entity.getPersistentID();
	}

	public EntityPlayerMP getVanillaEntity() {
		return entity;
	}

	public LogicalServer getLogicalServer() {
		return this.server;
	}

}
