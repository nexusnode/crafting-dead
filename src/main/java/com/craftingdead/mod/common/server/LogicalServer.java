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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Contains common logic for CraftingDead, implemented by the physical client
 * and physical server as integrated and dedicated servers
 * 
 * @author Sm0keySa1m0n
 *
 */
@EventBusSubscriber(value = Side.SERVER, modid = CraftingDead.MOD_ID)
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
	protected Map<EntityPlayerMP, Player> playerMap = new HashMap<EntityPlayerMP, Player>();

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
	public void onServerTick(ServerTickEvent event) {
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

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		CraftingDead.instance().getNetworkWrapper().sendTo(new SPacketRequestHandshake(),
				(EntityPlayerMP) event.player);
		handshakeTimeoutMap.put((EntityPlayerMP) event.player, HANDSHAKE_TIMEOUT);
	}

	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
		this.playerMap.remove((EntityPlayerMP) event.player);
	}

	// ================================================================================
	// Normal Methods
	// ================================================================================

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

	/**
	 * Called to verify if the supplied {@link CPacketHandshake} should be accepted
	 * thus allowing the player to join
	 * 
	 * @param handshakeMessage - the {@link CPacketHandshake} instance
	 * @return if its accepted
	 */
	protected abstract boolean verifyHandshake(CPacketHandshake handshakeMessage);

	/**
	 * Called when the player has been accepted into the game
	 * 
	 * @param player - the {@link Player} instance
	 */
	protected abstract void playerAccepted(Player player);

	// ================================================================================
	// Getters
	// ================================================================================

	public Player getPlayer(EntityPlayerMP player) {
		return this.playerMap.get(player);
	}

}
