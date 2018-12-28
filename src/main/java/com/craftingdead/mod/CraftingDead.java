package com.craftingdead.mod;

import java.io.File;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.capability.triggerable.Triggerable;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.init.ModCapabilities;
import com.craftingdead.mod.network.message.client.SetTriggerPressedCMessage;
import com.craftingdead.mod.network.message.server.SetTriggerPressedSMessage;
import com.craftingdead.mod.network.message.server.UpdateStatisticsSMessage;
import com.craftingdead.mod.server.LogicalServer;
import com.craftingdead.mod.server.dedicated.ServerDist;
import com.craftingdead.mod.tileentity.TileEntityLoot;
import com.craftingdead.mod.util.DistExecutor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The main mod class for Crafting Dead.
 * 
 * @author Sm0keySa1m0n
 *
 */
@Mod(modid = CraftingDead.MOD_ID)
public class CraftingDead {
	/**
	 * Mod ID
	 */
	public static final String MOD_ID = "craftingdead";
	/**
	 * Used for internal networking
	 */
	public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
	/**
	 * {@link Logger} instance
	 */
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * Singleton
	 */
	private static CraftingDead instance;
	/**
	 * Mod metadata
	 */
	private ModMetadata metadata;
	/**
	 * The {@link ModDist} instance
	 */
	private ModDist modDist;
	/**
	 * The data folder
	 */
	private File modFolder;
	/**
	 * {@link LogicalServer} instance
	 */
	private LogicalServer logicalServer;

	private CraftingDead() {
		instance = this;

		this.modFolder = new File((File) FMLInjectionData.data()[6], CraftingDead.MOD_ID);
		this.modFolder.mkdir();

		this.modDist = DistExecutor.runForDist(() -> ClientDist::new, () -> ServerDist::new);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			LOGGER.info("Shutting down");
			CraftingDead.this.modDist.shutdown();
		}));
	}

	// ================================================================================
	// FML Events
	// ================================================================================

	@Mod.EventHandler
	public void onEvent(FMLPreInitializationEvent event) {
		LOGGER.info("Processing {}", event.description());

		this.metadata = event.getModMetadata();

		LOGGER.info("Registering messages");
		this.registerMessages();

		LOGGER.info("Registering tile entities");
		this.registerTileEntities();

		LOGGER.info("Registering capabilities");
		ModCapabilities.registerCapabilities();

		this.modDist.preInitialization(event);
	}

	@Mod.EventHandler
	public void onEvent(FMLInitializationEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.modDist.initialization(event);
	}

	@Mod.EventHandler
	public void onEvent(FMLPostInitializationEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.modDist.postInitialization(event);
	}

	@Mod.EventHandler
	public void onEvent(FMLLoadCompleteEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.modDist.loadComplete(event);
	}

	@Mod.EventHandler
	public void onEvent(FMLServerStartingEvent event) {
		LOGGER.info("Processing {}", event.description());
		this.logicalServer = this.modDist.getLogicalServerSupplier().get();
		MinecraftForge.EVENT_BUS.register(this.logicalServer);
	}

	@Mod.EventHandler
	public void onEvent(FMLServerStoppingEvent event) {
		LOGGER.info("Processing {}", event.description());
		if (this.logicalServer != null) {
			MinecraftForge.EVENT_BUS.unregister(this.logicalServer);
			this.logicalServer = null;
		}
	}

	@NetworkCheckHandler
	public boolean networkCheck(Map<String, String> mods, Side side) {
		return this.modDist.networkCheck(mods, side);
	}

	// ================================================================================
	// Bootstrap Methods
	// ================================================================================

	private void registerMessages() {
		int discriminator = -1;
		NETWORK_WRAPPER.registerMessage(UpdateStatisticsSMessage.UpdateStatisticsSHandler.class,
				UpdateStatisticsSMessage.class, discriminator++, Side.CLIENT);
		NETWORK_WRAPPER.registerMessage(SetTriggerPressedSMessage.SetTriggerPressedSHandler.class,
				SetTriggerPressedSMessage.class, discriminator++, Side.CLIENT);

		NETWORK_WRAPPER.registerMessage(SetTriggerPressedCMessage.SetTriggerPressedCHandler.class,
				SetTriggerPressedCMessage.class, discriminator++, Side.SERVER);

	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TileEntityLoot.class, new ResourceLocation(CraftingDead.MOD_ID, "loot"));
	}

	// ================================================================================
	// Getters
	// ================================================================================

	public ModMetadata getMetadata() {
		return this.metadata;
	}

	public ModDist getModDist() {
		return this.modDist;
	}

	@Nullable
	public LogicalServer getLogicalServer() {
		return this.logicalServer;
	}

	public File getModFolder() {
		return this.modFolder;
	}

	// ================================================================================
	// Static Methods
	// ================================================================================

	@Mod.InstanceFactory
	public static CraftingDead instance() {
		return instance != null ? instance : new CraftingDead();
	}

	// ================================================================================
	// Common Forge Events
	// ================================================================================

	@Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID)
	public static class Events {

		@SubscribeEvent
		public static void onEvent(TickEvent.PlayerTickEvent event) {
			if (event.phase == Phase.END)
				event.player.getCapability(ModCapabilities.PLAYER, null).update();
		}

		@SubscribeEvent
		public static void onEvent(LivingDeathEvent event) {
			if (event.getSource().getTrueSource() instanceof EntityPlayer)
				event.getSource().getTrueSource().getCapability(ModCapabilities.PLAYER, null).onKill(event.getEntity(),
						event.getSource());
		}

		@SubscribeEvent
		public static void onEvent(LivingEvent.LivingUpdateEvent event) {
			ItemStack itemStack = event.getEntityLiving().getHeldItemMainhand();
			Triggerable triggerable = itemStack.getCapability(ModCapabilities.TRIGGERABLE, null);
			if (triggerable != null)
				triggerable.update(itemStack, event.getEntity());
		}

	}

}
