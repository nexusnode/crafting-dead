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

import com.craftingdead.immerse.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import io.noties.tumbleweed.TweenType;
import net.minecraft.util.ResourceLocation;

public class ImageComponent extends Component<ImageComponent> {

  public static final TweenType<ImageComponent> COLOUR =
      new SimpleTweenType<>(4, t -> t.colour.getColour4f(), (t, v) -> t.colour.setColour4f(v));

  private final ResourceLocation image;
  private final Colour colour;

  public ImageComponent(ResourceLocation image) {
    this(image, new Colour());
  }

  public ImageComponent(ResourceLocation image, Colour colour) {
    this.image = image;
    this.colour = colour;
  }

  public Colour getColour() {
    return this.colour;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    RenderSystem.enableBlend();
    final float[] colour = this.colour.getColour4f();
    RenderSystem.color4f(colour[0], colour[1], colour[2], colour[3]);
    RenderUtil.bind(this.image);
    RenderUtil.blit(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();
  }
}
