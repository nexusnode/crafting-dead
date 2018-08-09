package com.craftingdead.mod.client;

import org.lwjgl.LWJGLException;

import com.craftingdead.mod.client.gui.GuiMainMenu;
import com.craftingdead.mod.core.CraftingDead;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientHooks {

	static ModClient client = CraftingDead.instance().getClient();
	static Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event) {
		if (event.getGui() instanceof net.minecraft.client.gui.GuiMainMenu) {
			event.setGui(new GuiMainMenu());
		}
	}
	

	@SubscribeEvent
	public static void onPreTooltipRender(RenderTooltipEvent.Color event) {
		event.setBackground(0xFF101010);
		event.setBorderEnd(0);
		event.setBorderStart(0);
	}

	// Fixes rendering crash - rendering entities when not inside a world
	@SubscribeEvent
	public static void onPreRenderEntitySpecials(RenderLivingEvent.Specials.Pre<?> event) {
		if (mc.player == null)
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase.equals(TickEvent.Phase.END))
			client.runTick();
	}

	public static void createDisplay(Minecraft mc) throws LWJGLException {
		client.createDisplay(mc);
	}

}
