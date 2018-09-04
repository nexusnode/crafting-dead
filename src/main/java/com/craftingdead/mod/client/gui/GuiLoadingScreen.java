package com.craftingdead.mod.client.gui;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import com.craftingdead.mod.client.renderer.LoadingGuiScreen;
import com.craftingdead.mod.common.core.CraftingDead;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;

public class GuiLoadingScreen extends LoadingGuiScreen {

	@Override
	protected void drawScreen(int mouseX, int mouseY) {
		this.drawImage(0, 0, this.width, this.height,
				new Texture(new ResourceLocation(CraftingDead.MOD_ID, "textures/gui/loading.png"), null), 1);

		ProgressBar first = null, penult = null, last = null;
		Iterator<ProgressBar> i = ProgressManager.barIterator();
		while (i.hasNext()) {
			if (first == null)
				first = i.next();
			else {
				penult = last;
				last = i.next();
			}
		}

		if (first != null) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);

			int x = this.width / 2;
			int y = this.height - this.height / 3;

			String line1 = first.getMessage();
			if (penult != null)
				line1 += ": " + penult.getMessage();
			this.fontRenderer.drawString(line1, x - this.fontRenderer.getStringWidth(line1) / 2, y, 0xFFFFFF);

			if (last != null) {
				this.fontRenderer.drawString(last.getMessage(),
						x - this.fontRenderer.getStringWidth(last.getMessage()) / 2,
						y + this.fontRenderer.FONT_HEIGHT * 2, 0xFFFFFF);
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}

	}

}
