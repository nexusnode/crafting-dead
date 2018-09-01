package com.craftingdead.mod.common.server;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.network.packet.PacketHandshake;
import com.craftingdead.mod.common.network.packet.PacketUpdateStatistics;
import com.recastproductions.network.packet.IPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Wraps around the vanilla {@link EntityPlayerMP} so we can associate our own
 * data, functions and logic. This class performs most of the logic associated
 * with the player and forwards data to the client's
 * {@link com.craftingdead.mod.client.PlayerSP} via packets
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
	 * The mods the player has installed on their client
	 */
	private String[] mods;
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

	public PlayerMP(LogicalServer server, EntityPlayerMP entity, PacketHandshake handshakeMessage) {
		this.server = server;
		this.entity = entity;
		this.mods = handshakeMessage.getMods();
	}

	public EntityPlayerMP getVanillaEntity() {
		return entity;
	}

	public String[] getMods() {
		return this.mods;
	}

	/**
	 * Send a packet to the player's client
	 * 
	 * @param packet - the {@link IPacket} to send
	 */
	public void sendPacket(IPacket packet) {
		CraftingDead.instance().getNetworkWrapper().sendTo(packet, this.getVanillaEntity());
	}

	public LogicalServer getLogicalServer() {
		return this.server;
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
		boolean isDay = entity.getEntityWorld().isDaytime();
		// If it was night time and is now day time then increment their days survived
		// by 1
		if (!lastDay && isDay) {
			daysSurvived++;
		}
		lastDay = isDay;

		if (this.daysSurvived != this.lastDaysSurvived || this.zombieKills != this.lastZombieKills
				|| this.playerKills != this.lastPlayerKills) {
			this.sendPacket(new PacketUpdateStatistics(this.daysSurvived, this.zombieKills, this.playerKills));
			this.lastDaysSurvived = this.daysSurvived;
			this.lastZombieKills = this.zombieKills;
			this.lastPlayerKills = this.playerKills;
		}
	}

	public void incrementPlayerKills() {
		this.playerKills++;
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

}
