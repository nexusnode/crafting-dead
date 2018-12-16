package com.craftingdead.mod.server;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.capability.SerializableProvider;
import com.craftingdead.mod.capability.player.ServerPlayer;
import com.craftingdead.mod.init.ModCapabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Contains common logic for the mod, implemented by the physical client and
 * physical server as integrated and dedicated servers allowing each side to
 * adjust the {@link LogicalServer} for side specific requirements
 * 
 * @author Sm0keySa1m0n
 *
 */
public abstract class LogicalServer {

	// ================================================================================
	// Logical Server Forge Events
	// ================================================================================

	@Mod.EventBusSubscriber(modid = CraftingDead.MOD_ID)
	public static class Events {

		@SubscribeEvent
		public static void onEvent(PlayerEvent.Clone event) {
			ServerPlayer that = (ServerPlayer) event.getOriginal().getCapability(ModCapabilities.PLAYER, null);
			ServerPlayer player = (ServerPlayer) event.getEntityPlayer().getCapability(ModCapabilities.PLAYER, null);
			player.copyFrom(that, event.isWasDeath());
		}

		@SubscribeEvent
		public static void onEvent(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof EntityPlayerMP) {
				ServerPlayer player = new ServerPlayer((EntityPlayerMP) event.getObject(),
						CraftingDead.instance().getLogicalServer());
				event.addCapability(new ResourceLocation(CraftingDead.MOD_ID, "player"),
						new SerializableProvider<>(player, ModCapabilities.PLAYER));
			}
		}
	}
}
