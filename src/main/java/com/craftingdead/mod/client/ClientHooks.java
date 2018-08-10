package com.craftingdead.mod.client;

import org.lwjgl.LWJGLException;

<<<<<<< HEAD
import com.craftingdead.mod.client.gui.GuiCDScreen;
import com.craftingdead.mod.client.gui.GuiMainMenu;
=======
import com.craftingdead.mod.client.gui.main.GuiMainMenu;
import com.craftingdead.mod.core.CraftingDead;
>>>>>>> 548c9a1c4c92ca63c793ac4ca0cae598f5d2ac76

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientHooks {
	
	private static ClientHooks instance;

	private final ModClient modClient;
	private final Minecraft mc;
	
	public ClientHooks(Minecraft mc, ModClient modClient) {
		instance = this;
		this.mc = mc;
		this.modClient = modClient;
	}
	
	@SubscribeEvent
	public void onGuiOpen(GuiOpenEvent event) {
		if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
			event.setGui(new GuiMainMenu());
		}
		if(event.getGui() instanceof GuiCDScreen) {
			((GuiCDScreen)event.getGui()).modClient = modClient;
		}
	}
	

	@SubscribeEvent
	public void onPreTooltipRender(RenderTooltipEvent.Color event) {
		event.setBackground(0xFF101010);
		event.setBorderEnd(0);
		event.setBorderStart(0);
	}

	// Fixes rendering crash - rendering entities when not inside a world
	@SubscribeEvent
	public void onPreRenderEntitySpecials(RenderLivingEvent.Specials.Pre<?> event) {
		if (mc.player == null)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END))
			modClient.runTick();
	}

	public void createDisplay(Minecraft mc) throws LWJGLException {
		modClient.createDisplay(mc);
	}

	public static ClientHooks instance() {
		return instance;
	}
	
}
