package com.craftingdead.mod.common.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.network.packet.client.CPacketHandshake;
import com.craftingdead.mod.common.network.packet.server.SPacketRequestHandshake;
import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public abstract class LogicalServer {

	private static final int HANDSHAKE_TIMEOUT = 200;

	private Map<EntityPlayerMP, Integer> handshakeTimeoutMap = new HashMap<EntityPlayerMP, Integer>();

	protected Map<EntityPlayerMP, Player> playerMap = new HashMap<EntityPlayerMP, Player>();

	private int maxPlayers;

	private World world;

	protected abstract void onServerStart(FMLServerStartingEvent event);

	protected abstract boolean verifyHandshake(CPacketHandshake handshakeMessage);

	protected abstract void playerAccepted(Player player);

	@Subscribe
	public void onPreInitialization(FMLPreInitializationEvent event) {
		ServerHooks.server = this;
		MinecraftForge.EVENT_BUS.register(ServerHooks.class);
	}

	@Subscribe
	public void serverStarting(FMLServerStartingEvent event) throws InstantiationException, IllegalAccessException {
		this.maxPlayers = event.getServer().getMaxPlayers();
		this.world = event.getServer().getEntityWorld();
		this.onServerStart(event);
	}

	void runTick() {
		Iterator<Map.Entry<EntityPlayerMP, Integer>> iter = handshakeTimeoutMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<EntityPlayerMP, Integer> entry = iter.next();
			entry.setValue(entry.getValue() - 1);
			if (entry.getValue() <= 0) {
				EntityPlayerMP player = entry.getKey();
				CraftingDead.LOGGER.warn(
						"{} failed to complete a successful handshake in a timely manner, disconnecting",
						player.getName());
				player.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.handshakeFail"));
				iter.remove();
			}
		}
	}

	void login(EntityPlayerMP entityPlayer) {
		CraftingDead.instance().getNetworkWrapper().sendTo(new SPacketRequestHandshake(), entityPlayer);
		handshakeTimeoutMap.put(entityPlayer, HANDSHAKE_TIMEOUT);
	}

	void logout(EntityPlayerMP player) {

	}

	public void onHandshake(EntityPlayerMP entity, CPacketHandshake handshakeMessage) {
		CraftingDead.LOGGER.info("Verifying handshake for {}", entity.getName());
		if (this.verifyHandshake(handshakeMessage)) {
			this.handshakeTimeoutMap.remove(entity);
			Player player = new Player(this, entity, handshakeMessage);
			this.playerMap.put(entity, player);
			this.playerAccepted(player);

			CraftingDead.LOGGER.info("{} has successfully passed the handshake verification", entity.getName());
		}
	}

	// ================================================================================
	// Getters
	// ================================================================================

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public World getWorld() {
		return this.world;
	}

	public Player getPlayer(EntityPlayerMP player) {
		return this.playerMap.get(player);
	}

}
