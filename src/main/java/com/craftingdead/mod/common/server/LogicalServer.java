package com.craftingdead.mod.common.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.network.packet.PacketHandshake;
import com.craftingdead.mod.common.network.packet.PacketRequestHandshake;
import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
	 * A timeout value used to decide how many ticks to wait for a handshake
	 * response until the player is kicked
	 */
	private static final int HANDSHAKE_TIMEOUT = 200;

	/**
	 * Updated every tick with players and their timeout count down
	 */
	private Map<EntityPlayerMP, Integer> handshakeTimeoutMap = new HashMap<EntityPlayerMP, Integer>();

	/**
	 * A map of all the players currently on the server
	 */
	protected Map<EntityPlayerMP, PlayerMP> playerMap = new HashMap<EntityPlayerMP, PlayerMP>();

	// ================================================================================
	// Mod Events
	// ================================================================================

	@Subscribe
	public void serverStarting(FMLServerStartingEvent event) throws InstantiationException, IllegalAccessException {
		CraftingDead.LOGGER.info("Starting {} logical server", CraftingDead.MOD_NAME);
		this.onServerStart(event);
	}

	/**
	 * Called when the server is starting
	 * 
	 * @param event - the {@link FMLServerStartingEvent} instance
	 */
	protected abstract void onServerStart(FMLServerStartingEvent event);

	// ================================================================================
	// Forge Events
	// ================================================================================

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		Iterator<Map.Entry<EntityPlayerMP, Integer>> iter = handshakeTimeoutMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<EntityPlayerMP, Integer> entry = iter.next();
			// Reduce the timer value
			entry.setValue(entry.getValue() - 1);
			// Kick the player if they have run out of time to send a valid handshake packet
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

	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player instanceof EntityPlayerMP) {
			EntityPlayerMP entity = (EntityPlayerMP) event.player;
			PlayerMP player = this.playerMap.get(entity);
			if (player != null) {
				player.update();
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP entity = (EntityPlayerMP) event.getEntityLiving();
			PlayerMP player = this.playerMap.get(entity);
			if (player != null) {
				player.onDeath(event.getSource());
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		// Ask the client to send their handshake packet
		CraftingDead.instance().getNetworkWrapper().sendTo(new PacketRequestHandshake(), (EntityPlayerMP) event.player);
		// Begin the timeout timer
		handshakeTimeoutMap.put((EntityPlayerMP) event.player, HANDSHAKE_TIMEOUT);
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
		PlayerMP player = this.getPlayer((EntityPlayerMP) event.player);
		// Save their data
		player.getVanillaEntity().getEntityData().setTag(CraftingDead.MOD_ID, player.serializeNBT());
		this.playerMap.remove((EntityPlayerMP) event.player);
	}

	// ================================================================================
	// Normal Methods
	// ================================================================================

	/**
	 * Called by the handshake packet handler to initiate a handshake
	 * 
	 * @param entity           - the player trying to login
	 * @param handshakeMessage - the {@link PacketHandshake} instance
	 */
	public void onHandshake(EntityPlayerMP entity, PacketHandshake handshakeMessage) {
		CraftingDead.LOGGER.info("Verifying handshake for {}", entity.getName());
		if (this.verifyHandshake(handshakeMessage)) {
			// Remove them from the handshake map or else they will be kicked
			this.handshakeTimeoutMap.remove(entity);
			// Create our custom Player object that wraps around the vanilla one
			PlayerMP player = new PlayerMP(this, entity, handshakeMessage);
			// Load their data (if any) from the world save
			player.deserializeNBT(entity.getEntityData().getCompoundTag(CraftingDead.MOD_ID));
			this.playerMap.put(entity, player);
			this.playerAccepted(player);
			CraftingDead.LOGGER.info("{} has successfully passed the handshake verification", entity.getName());
		}
	}

	/**
	 * Called to verify if the supplied {@link PacketHandshake} should be accepted
	 * thus allowing the player to join
	 * 
	 * @param handshakePacket - the {@link PacketHandshake} instance
	 * @return if its accepted
	 */
	protected abstract boolean verifyHandshake(PacketHandshake handshakePacket);

	/**
	 * Called when the player has been accepted into the game
	 * 
	 * @param player - the {@link PlayerMP} instance
	 */
	protected abstract void playerAccepted(PlayerMP player);

	// ================================================================================
	// Getters
	// ================================================================================

	public PlayerMP getPlayer(EntityPlayerMP player) {
		return this.playerMap.get(player);
	}

}
