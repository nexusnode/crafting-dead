package com.craftingdead.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ServerPlayer;
import com.craftingdead.mod.client.ClientDist;
import com.craftingdead.mod.message.client.ClientTriggerPressedMessage;
import com.craftingdead.mod.message.server.SUpdateStatisticsMessage;
import com.craftingdead.mod.message.server.ServerTriggerPressedMessage;
import com.craftingdead.mod.server.ServerDist;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.forgespi.language.IModInfo;

@Mod(CraftingDead.MOD_ID)
public class CraftingDead {
	/**
	 * Mod ID
	 */
	public static final String MOD_ID = "craftingdead", NETWORK_VERSION = "0.0.1";
	/**
	 * Main network channel
	 */
	public static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.ChannelBuilder //
			.named(new ResourceLocation(CraftingDead.MOD_ID, "main")) //
			.clientAcceptedVersions(NETWORK_VERSION::equals) //
			.serverAcceptedVersions(NETWORK_VERSION::equals) //
			.networkProtocolVersion(() -> NETWORK_VERSION) //
			.simpleChannel();
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * Singleton
	 */
	@Getter
	private static CraftingDead instance;
	/**
	 * Mod info
	 */
	@Getter
	private final IModInfo modInfo;
	/**
	 * Event bus
	 */
	@Getter
	private final IEventBus eventBus;
	/**
	 * Mod distribution
	 */
	@Getter
	private final IModDist modDist;

	public CraftingDead() {
		instance = this;
		this.modInfo = ModLoadingContext.get().getActiveContainer().getModInfo();
		this.eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		this.modDist = DistExecutor.runForDist(() -> ClientDist::new, () -> ServerDist::new);
		this.eventBus.addListener(this::setup);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CommonConfig.CLIENT_SPEC);
	}

	private void setup(FMLCommonSetupEvent event) {
		LOGGER.info("Registering messages");
		this.registerMessages();

		LOGGER.info("Registering capabilities");
		ModCapabilities.registerCapabilities();
	}

	private void registerMessages() {
		int discriminator = -1;
		NETWORK_CHANNEL.messageBuilder(SUpdateStatisticsMessage.class, discriminator++) //
				.encoder((msg, buffer) -> {
					buffer.writeInt(msg.getDaysSurvived());
					buffer.writeInt(msg.getZombiesKilled());
					buffer.writeInt(msg.getPlayersKilled());
				}) //
				.decoder((buffer) -> {
					int daysSurvived = buffer.readInt(), zombiesKilled = buffer.readInt(),
							playersKilled = buffer.readInt();
					return new SUpdateStatisticsMessage(daysSurvived, zombiesKilled, playersKilled);
				}) //
				.consumer((msg, ctx) -> {
					((ClientDist) CraftingDead.getInstance().getModDist()).getPlayer().ifPresent((player) -> player
							.updateStatistics(msg.getDaysSurvived(), msg.getZombiesKilled(), msg.getPlayersKilled()));
				}) //
				.add();
		NETWORK_CHANNEL.messageBuilder(ServerTriggerPressedMessage.class, discriminator++) //
				.encoder((msg, buffer) -> {
					buffer.writeInt(msg.getEntityId());
					buffer.writeBoolean(msg.isTriggerPressed());
				}) //
				.decoder((buffer) -> {
					int entityId = buffer.readInt();
					boolean triggerPressed = buffer.readBoolean();
					return new ServerTriggerPressedMessage(entityId, triggerPressed);
				}) //
				.consumer((msg, ctx) -> {
					Entity entity = Minecraft.getInstance().world.getEntityByID(msg.getEntityId());
					if (entity != null && entity instanceof AbstractClientPlayerEntity)
						entity.getCapability(ModCapabilities.PLAYER, null)
								.ifPresent((player) -> player.setTriggerPressed(msg.isTriggerPressed()));
				}) //
				.add();
		NETWORK_CHANNEL.messageBuilder(ClientTriggerPressedMessage.class, discriminator++) //
				.encoder((msg, buffer) -> {
					buffer.writeBoolean(msg.isTriggerPressed());
				}) //
				.decoder((buffer) -> {
					boolean triggerPressed = buffer.readBoolean();
					return new ClientTriggerPressedMessage(triggerPressed);
				}) //
				.consumer((msg, ctx) -> {
					ctx.get().getSender().getCapability(ModCapabilities.PLAYER, null)
							.ifPresent((player) -> player.setTriggerPressed(msg.isTriggerPressed()));
				}) //
				.add();
	}

	// ================================================================================
	// Common Forge Events
	// ================================================================================

	@Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID)
	public static class Events {

		@SubscribeEvent
		public static void onEvent(TickEvent.PlayerTickEvent event) {
			if (event.phase == Phase.END)
				event.player.getCapability(ModCapabilities.PLAYER, null).ifPresent((player) -> player.update());
		}

		@SubscribeEvent
		public static void onEvent(LivingDeathEvent event) {
			if (!event.isCanceled() && event.getEntity() instanceof PlayerEntity) {
				event.getEntity().getCapability(ModCapabilities.PLAYER, null)
						.ifPresent((player) -> event.setCanceled(player.onDeath(event.getSource())));
			}
			if (!event.isCanceled() && event.getSource().getTrueSource() instanceof PlayerEntity) {
				event.getSource().getTrueSource().getCapability(ModCapabilities.PLAYER, null)
						.ifPresent((player) -> event.setCanceled(player.onKill(event.getEntity())));
			}
		}

		@SubscribeEvent
		public static void onEvent(LivingEvent.LivingUpdateEvent event) {
			ItemStack itemStack = event.getEntityLiving().getHeldItemMainhand();
			itemStack.getCapability(ModCapabilities.TRIGGERABLE, null)
					.ifPresent((triggerable) -> triggerable.update(itemStack, event.getEntity()));
		}

		@SubscribeEvent
		public static void onEvent(PlayerEvent.Clone event) {
			event.getEntityPlayer().getCapability(ModCapabilities.PLAYER, null).<ServerPlayer>cast()
					.ifPresent((player) -> {
						event.getOriginal().getCapability(ModCapabilities.PLAYER, null).<ServerPlayer>cast()
								.ifPresent((that) -> {
									player.copyFrom(that, event.isWasDeath());
								});
					});
		}

		@SubscribeEvent
		public static void onEvent(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof ServerPlayerEntity) {
				ServerPlayer player = new ServerPlayer((ServerPlayerEntity) event.getObject());
				event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"),
						new SerializableProvider<>(player, ModCapabilities.PLAYER));
			}
		}
	}
}
