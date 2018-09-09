package com.craftingdead.mod.common.multiplayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.mod.common.event.server.HandshakeEvent;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketHandshake;
import com.craftingdead.mod.common.multiplayer.network.packet.PacketRequestHandshake;
import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

/**
 * Contains common logic for the mod, implemented by the physical client and
 * physical server as integrated and dedicated servers allowing each side to
 * adjust the {@link LogicalServer} for side specific requirements
 * 
 * @author Sm0keySa1m0n
 *
 */
public abstract class LogicalServer<M extends ISidedMod<M, ?>> {

	/**
	 * The mod instance
	 */
	private M mod;

	/**
	 * Updated every tick with players and their timeout count down
	 */
	private Map<EntityPlayerMP, Integer> handshakeTimeoutMap = new HashMap<EntityPlayerMP, Integer>();

	/**
	 * A map of all the players currently on the server
	 */
	protected Map<EntityPlayerMP, PlayerMP> playerMap = new HashMap<EntityPlayerMP, PlayerMP>();

	public void start(M mod, MinecraftServer server) {
		this.mod = mod;
		CraftingDead.LOGGER.info("Starting {} logical server", CraftingDead.MOD_NAME);
	}

	// ================================================================================
	// Mod Events
	// ================================================================================

	@Subscribe
	public void onHandshake(HandshakeEvent event) {
		EntityPlayerMP entity = event.getPlayer();
		PacketHandshake handshakePacket = event.getPacketHandshake();
		CraftingDead.LOGGER.info("Verifying handshake for {}", entity.getName());
		if (this.verifyHandshake(handshakePacket)) {
			// Remove them from the handshake map or else they will be kicked
			this.handshakeTimeoutMap.remove(entity);
			// Create our custom Player object that wraps around the vanilla one
			PlayerMP player = new PlayerMP(this, entity, handshakePacket);
			// Load their data (if any) from the world save
			player.deserializeNBT(entity.getEntityData().getCompoundTag(CraftingDead.MOD_ID));
			this.playerMap.put(entity, player);
			// Set their game type to the cached version
			entity.setGameType(GameType.valueOf(entity.getEntityData().getString("gameTypeCache")));
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
	// Forge Events
	// ================================================================================

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		Iterator<Map.Entry<EntityPlayerMP, Integer>> iter = handshakeTimeoutMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<EntityPlayerMP, Integer> entry = iter.next();
			EntityPlayerMP player = entry.getKey();
			// Reduce the timer value
			entry.setValue(entry.getValue() - 1);
			// Kick the player if they have run out of time to send a valid handshake packet
			if (entry.getValue() <= 0) {
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
			// If the player being killed hasn't completed their handshake it will be null
			if (player != null) {
				player.onDeath(event.getSource());
				if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
					EntityPlayerMP causeEntity = (EntityPlayerMP) event.getSource().getTrueSource();
					PlayerMP cause = this.playerMap.get(causeEntity);
					// If the cause player hasn't completed their handshake it will be null
					if (cause != null) {
						event.setCanceled(this.onPlayerKillPlayer(player, cause));
						if (!event.isCanceled())
							cause.incrementPlayerKills();
					} else {
						// Cancel the event because the cause player hasn't completed their handshake
						event.setCanceled(true);
					}
				}
			} else {
				// Cancel the event because the player being killed hasn't completed their
				// handshake
				event.setCanceled(true);
			}
		} else if (event.getEntityLiving() instanceof EntityZombie) {
			if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
				EntityPlayerMP causeEntity = (EntityPlayerMP) event.getSource().getTrueSource();
				PlayerMP cause = this.playerMap.get(causeEntity);
				// If the cause player hasn't completed their handshake it will be null
				if (cause != null) {
					event.setCanceled(this.onPlayerKillZombie(cause));
					if (!event.isCanceled())
						cause.incrementZombieKills();
				} else {
					// Cancel the event because the cause player hasn't completed their handshake
					event.setCanceled(true);
				}
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
	 * @return if the event should be cancelledF
	 */
	protected abstract boolean onPlayerKillZombie(PlayerMP cause);

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		// Cache their game type so we can restore it once they complete the handshake
		player.getEntityData().setString("gameTypeCache", player.interactionManager.getGameType().name());
		// Set them to spectator mode as they haven't 'truly' joined yet
		player.setGameType(GameType.SPECTATOR);
		// Ask the client to send their handshake packet
		CraftingDead.instance().getNetworkWrapper().sendTo(new PacketRequestHandshake(), (EntityPlayerMP) event.player);
		// Begin the timeout timer
		handshakeTimeoutMap.put((EntityPlayerMP) event.player, this.getHandshakeTimeout());
	}

	protected abstract int getHandshakeTimeout();

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

	public M getMod() {
		return this.mod;
	}

}
