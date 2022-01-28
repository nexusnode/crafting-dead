/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
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

package com.craftingdead.immerse.client.gui.view;

import java.util.Objects;
import com.craftingdead.immerse.client.gui.view.layout.Layout;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.util.ResourceLocation;

public class PanoramaView<L extends Layout> extends View<PanoramaView<L>, L> {

  private final RenderSkybox panorama;

  private final ResourceLocation panoramaTexture;

  public PanoramaView(L layout, ResourceLocation panoramaTexture) {
    super(layout);
    Objects.requireNonNull(panoramaTexture, "Panorama texture cannot be null");
    this.panoramaTexture = panoramaTexture;
    this.panorama = new RenderSkybox(new RenderSkyboxCube(this.panoramaTexture));
  }

  @Override
  public void renderContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(matrixStack, mouseX, mouseY, partialTicks);
    this.panorama.render(partialTicks, this.getAlpha());
  }
}
