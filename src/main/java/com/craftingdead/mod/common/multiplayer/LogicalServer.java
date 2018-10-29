package com.craftingdead.mod.common.multiplayer;

import java.util.HashMap;
import java.util.Map;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.Proxy;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

/**
 * Contains common logic for the mod, implemented by the physical client and
 * physical server as integrated and dedicated servers allowing each side to
 * adjust the {@link LogicalServer} for side specific requirements
 * 
 * @author Sm0keySa1m0n
 *
 */
public abstract class LogicalServer {

	/**
	 * The {@link Proxy}
	 */
	private Proxy proxy;
	/**
	 * The {@link MinecraftServer}
	 */
	protected MinecraftServer minecraftServer;
	/**
	 * A map of all the players currently on the server
	 */
	protected Map<EntityPlayerMP, PlayerMP> playerMap = new HashMap<EntityPlayerMP, PlayerMP>();

	public void start(Proxy proxy, MinecraftServer minecraftServer) {
		this.proxy = proxy;
		this.minecraftServer = minecraftServer;
		CraftingDead.LOGGER.info("Starting logical server");
	}

	// ================================================================================
	// Forge Events
	// ================================================================================

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		EntityPlayerMP entity = (EntityPlayerMP) event.player;
		NetworkDispatcher networkDispatcher = NetworkDispatcher.get(entity.connection.netManager);
		if (this.checkModList(networkDispatcher.getModList())) {
			PlayerMP player = new PlayerMP(this, entity);
			// Load their data (if any) from the world save
			player.deserializeNBT(entity.getEntityData().getCompoundTag(CraftingDead.MOD_ID));
			this.playerMap.put(entity, player);
			this.onPlayerAccepted(player);
			CraftingDead.LOGGER.info("{} has successfully passed the handshake check", entity.getName());
		} else {
			CraftingDead.LOGGER.warn("{} failed the handshake check, disconnecting", entity.getName());
			entity.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.handshake_fail"));
		}
	}

	/**
	 * Check the mod list
	 * 
	 * @param mods - the list
	 * @return if the client should be accepted into the game
	 */
	protected abstract boolean checkModList(Map<String, String> mods);

	/**
	 * Called when the player has been accepted into the game
	 * 
	 * @param player - the {@link PlayerMP} instance
	 */
	protected abstract void onPlayerAccepted(PlayerMP player);

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			if (event.player instanceof EntityPlayerMP) {
				EntityPlayerMP entity = (EntityPlayerMP) event.player;
				PlayerMP player = this.playerMap.get(entity);
				if (player != null) {
					player.update();
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP entity = (EntityPlayerMP) event.getEntityLiving();
			PlayerMP player = this.playerMap.get(entity);
			player.onDeath(event.getSource());
			if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
				EntityPlayerMP causeEntity = (EntityPlayerMP) event.getSource().getTrueSource();
				PlayerMP cause = this.playerMap.get(causeEntity);
				event.setCanceled(this.onPlayerKillPlayer(player, cause));
				if (!event.isCanceled())
					cause.incrementPlayerKills();
			}
		} else if (event.getEntityLiving() instanceof EntityZombie) {
			if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
				EntityPlayerMP causeEntity = (EntityPlayerMP) event.getSource().getTrueSource();
				PlayerMP cause = this.playerMap.get(causeEntity);
				event.setCanceled(this.onPlayerKillZombie(cause));
				if (!event.isCanceled())
					cause.incrementZombieKills();
			}
		}
	}

	/**
	 * When a player kills another player
	 * 
	 * @param player - the player that died
	 * @param cause  - the killer
	 * @return if the event should be cancelled
	 */
	protected abstract boolean onPlayerKillPlayer(PlayerMP player, PlayerMP cause);

	/**
	 * When a player kills a zombie
	 * 
	 * @param cause - the killer
	 * @return if the event should be cancelled
	 */
	protected abstract boolean onPlayerKillZombie(PlayerMP cause);

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
		PlayerMP player = this.playerMap.get((EntityPlayerMP) event.player);
		// Player may not of completed their handshake so could be null
		if (player != null) {
			// Save their data
			player.getVanillaEntity().getEntityData().setTag(CraftingDead.MOD_ID, player.serializeNBT());
			this.playerMap.remove((EntityPlayerMP) event.player);
		}
	}

	// ================================================================================
	// Getters
	// ================================================================================

	public PlayerMP getPlayer(EntityPlayerMP player) {
		return this.playerMap.get(player);
	}

	public Proxy getMod() {
		return this.proxy;
	}

}
