/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftingdead.immerse.client.gui.component;

import java.util.Objects;
import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.util.ResourceLocation;

public class PanoramaComponent extends Component<PanoramaComponent> {

  private static final ResourceLocation PANORAMA_OVERLAY_TEXTURES =
      new ResourceLocation("textures/gui/title/background/panorama_overlay.png");

  private final RenderSkybox panorama;

  private final ResourceLocation panoramaTexture;

  public PanoramaComponent(ResourceLocation panoramaTexture) {
    Objects.requireNonNull(panoramaTexture, "Panorama texture cannot be null");
    this.panoramaTexture = panoramaTexture;
    this.panorama = new RenderSkybox(new RenderSkyboxCube(this.panoramaTexture));
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.panorama.render(partialTicks, 1.0F);
    RenderUtil.bind(PANORAMA_OVERLAY_TEXTURES);
    RenderSystem.enableBlend();
    RenderSystem
        .blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderUtil
        .blit(this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0.0F, 0.0F, 16, 128, 16,
            128);
    RenderSystem.disableBlend();
  }
}
