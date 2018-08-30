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
	 * Used to along with {@link #lastWorldTime} to calculate {@link #timeAlive}
	 */
	private long worldTime;
	/**
	 * Used along with {@link #worldTime} to calculate {@link #timeAlive}
	 */
	private long lastWorldTime;
	/**
	 * Used to calculate {@link #daysSurvived}
	 */
	private int timeAlive;
	/**
	 * Days survived
	 */
	private int daysSurvived;
	/**
	 * Used to detect if {@link #daysSurvived} has changed and needs to be sent to
	 * the client
	 */
	private int lastDaysSurvived = -1;

	public PlayerMP(LogicalServer server, EntityPlayerMP entity, PacketHandshake handshakeMessage) {
		this.server = server;
		this.entity = entity;
		this.mods = handshakeMessage.getMods();
		this.worldTime = entity.getEntityWorld().getWorldTime();
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
		return nbt;
	}

	/**
	 * Deserialise the class from an {@link NBTTagCompound} which is usually read
	 * from {@link EntityPlayerMP#getEntityData()}
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.timeAlive = nbt.getInteger("timeAlive");
		this.daysSurvived = nbt.getInteger("daysSurvived");
	}

	/**
	 * Update the player
	 */
	@Override
	public void update() {
		lastWorldTime = worldTime;
		worldTime = entity.getEntityWorld().getWorldTime();
		if (worldTime != lastWorldTime) {
			System.out.println("test" + timeAlive);
			timeAlive += worldTime - lastWorldTime;
		}
		daysSurvived = timeAlive / 24000;
		if (this.daysSurvived != this.lastDaysSurvived) {
			this.sendPacket(new PacketUpdateStatistics(this.daysSurvived));
			this.lastDaysSurvived = this.daysSurvived;
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
	}

}
