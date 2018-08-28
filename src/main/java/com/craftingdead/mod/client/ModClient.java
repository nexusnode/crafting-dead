package com.craftingdead.mod.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;

import com.craftingdead.mod.common.core.CDDummyContainer;
import com.craftingdead.mod.common.core.CraftingDead;
import com.craftingdead.mod.common.core.ISidedMod;
import com.craftingdead.mod.common.network.packet.client.CPacketHandshake;
import com.craftingdead.network.modclient.NetClientHandlerModClient;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.recastproductions.network.NetworkClient;
import com.recastproductions.network.impl.client.NetworkRegistryClient;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLContainer;
import net.minecraftforge.fml.common.InjectedModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MCPDummyContainer;
import net.minecraftforge.fml.common.MinecraftDummyContainer;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public final class ModClient implements ISidedMod<IntegratedServer> {

	private static final String[] ICON_LOCATIONS = new String[] {
			"assets/craftingdead/textures/gui/icons/icon_16x16.png",
			"assets/craftingdead/textures/gui/icons/icon_32x32.png",
			"assets/craftingdead/textures/gui/icons/icon_64x64.png" };

	private static final ImmutableList<Class<? extends ModContainer>> AUTHORIZED_MOD_CONTAINERS = new ImmutableList.Builder<Class<? extends ModContainer>>()
			.add(CDDummyContainer.class).add(MinecraftDummyContainer.class).add(FMLContainer.class)
			.add(ForgeModContainer.class).add(MCPDummyContainer.class).build();
	
	private IntegratedServer integratedServer;
	
	private ClientHooks clientHooks;
	
	private NetClientHandlerModClient netHandler = new NetClientHandlerModClient(this);
	private NetworkRegistryClient registryClient = new NetworkRegistryClient(netHandler);
	private NetworkClient networkClient = new NetworkClient(registryClient);
	
	public ModClient() {
		netHandler = new NetClientHandlerModClient(this);
		registryClient = new NetworkRegistryClient(netHandler);
		networkClient = new NetworkClient(registryClient.getChannelInitializer());
	}

	@Override
	public void setup(CraftingDead mod) {
		integratedServer = new IntegratedServer();
		clientHooks = new ClientHooks(Minecraft.getMinecraft(), this);
	}

	@Override
	public IntegratedServer getLogicalServer() {
		return integratedServer;
	}

	@Override
	public void onShutdown() {
		;
	}

	@Subscribe
	public void preInitializationEvent(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(clientHooks);
		networkClient.connect(CraftingDead.MANAGEMENT_SERVER_ADDRESS);
	}
	
	void runTick() {

	}

	void createDisplay(Minecraft mc) throws LWJGLException {
		mc.gameSettings.guiScale = 2;
		Display.setTitle(CraftingDead.MOD_NAME);
		Display.setResizable(false);

		List<ByteBuffer> icons = new ArrayList<ByteBuffer>();
		for (String location : ICON_LOCATIONS) {
			try {
				ClassLoader classloader = Thread.currentThread().getContextClassLoader();
				InputStream inputstream = classloader.getResourceAsStream(location);
				icons.add(readImageToBuffer(inputstream));
				inputstream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Display.setIcon(icons.toArray(new ByteBuffer[0]));

		try {
			Display.create((new PixelFormat()).withDepthBits(24));
		} catch (LWJGLException lwjglexception) {
			CraftingDead.LOGGER.error("Couldn't set pixel format", (Throwable) lwjglexception);

			try {
				Thread.sleep(1000L);
			} catch (InterruptedException var3) {
				;
			}

			if (mc.isFullScreen()) {
				mc.updateDisplayMode();
			}

			Display.create();
		}
	}

	public static ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
		BufferedImage bufferedimage = ImageIO.read(imageStream);
		int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), (int[]) null, 0,
				bufferedimage.getWidth());
		ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);

		for (int i : aint) {
			bytebuffer.putInt(i << 8 | i >> 24 & 255);
		}

		bytebuffer.flip();
		return bytebuffer;
	}

	public CPacketHandshake getHandshakePacket() {
		List<String> unauthorizedMods = new ArrayList<String>();
		for (ModContainer mod : Loader.instance().getModList()) {
			if (mod instanceof InjectedModContainer) {
				InjectedModContainer injectedMod = (InjectedModContainer) mod;
				mod = injectedMod.wrappedContainer;
			}
			if (AUTHORIZED_MOD_CONTAINERS.contains(mod.getClass())) {
				continue;
			}
			CraftingDead.LOGGER.warn("Found unauthorised mod container: " + mod.getName());
			unauthorizedMods.add(mod.getModId());
		}
		return new CPacketHandshake(unauthorizedMods.toArray(new String[0]));
	}
	
	public NetClientHandlerModClient getNetHandler() {
		return this.netHandler;
	}

}
