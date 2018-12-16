package com.craftingdead.mod.server.dedicated;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.function.Supplier;

import com.craftingdead.mod.CraftingDead;
import com.craftingdead.mod.ModDist;
import com.craftingdead.mod.masterserver.message.server.MessageKilledZombie;
import com.craftingdead.mod.masterserver.session.ServerSession;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;
import sm0keysa1m0n.network.wrapper.NetworkManager;

public class ServerDist implements ModDist {

	private MinecraftServer minecraftServer;

	@Override
	public void preInitialization(FMLPreInitializationEvent event) {
		this.minecraftServer = FMLServerHandler.instance().getServer();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean networkCheck(Map<String, String> mods, Side side) {
		return false;
	}

	@Override
	public Supplier<DedicatedServer> getLogicalServerSupplier() {
		return DedicatedServer::new;
	}

	@Override
	public InetSocketAddress getMasterServerAddress() {
		return new InetSocketAddress("localhost", 32888);
	}

	@Override
	public boolean useEpoll() {
		return minecraftServer.shouldUseNativeTransport();
	}

	@Override
	public ServerSession newSession() {
		return new ServerSession();
	}

	@Mod.EventBusSubscriber(value = Side.SERVER, modid = CraftingDead.MOD_ID)
	public static class Events {

		@SubscribeEvent
		public static void onEvent(LivingDeathEvent event) {
			if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
				NetworkManager networkManager = CraftingDead.instance().getNetworkManager();
				if (networkManager != null)
					networkManager
							.sendMessage(new MessageKilledZombie(event.getSource().getTrueSource().getPersistentID()));
			}
		}
	}
}
