/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.client.gui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Function;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CompositeButton extends Button {

  private final ResourceLocation textureLocation;
  private final int atlasX;
  private final int atlasY;
  private final int atlasXHover;
  private final int atlasYHover;
  private final int atlasXInactive;
  private final int atlasYInactive;

  private CompositeButton(Builder builder) {
    super(builder.x, builder.y, builder.width, builder.height, builder.text, builder.action);
    this.textureLocation = builder.textureLocation;
    this.atlasX = builder.atlasX;
    this.atlasY = builder.atlasY;
    this.atlasXHover = builder.atlasXHover;
    this.atlasYHover = builder.atlasYHover;
    this.atlasXInactive = builder.atlasXInactive;
    this.atlasYInactive = builder.atlasYInactive;
  }

  public static Builder button(int x, int y, int width, int height,
      ResourceLocation textureLocation) {
    return new Builder(x, y, width, height, textureLocation, CompositeButton::new);
  }

  @Override
  public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY,
      float partialTicks) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
    RenderSystem.setShaderTexture(0, this.textureLocation);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.enableDepthTest();
    if (this.isActive()) {
      if (this.isHoveredOrFocused()) {
        blit(matrixStack, this.x, this.y, this.atlasXHover, this.atlasYHover, this.width,
            this.height);
      } else {
        blit(matrixStack, this.x, this.y, this.atlasX, this.atlasY, this.width, this.height);
      }
    } else {
      blit(matrixStack, this.x, this.y, this.atlasXInactive, this.atlasYInactive, this.width,
          this.height);
    }
  }

  public static class Builder {

    private final Function<Builder, CompositeButton> factory;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final ResourceLocation textureLocation;
    private int atlasX;
    private int atlasY;
    private int atlasXHover;
    private int atlasYHover;
    private int atlasXInactive;
    private int atlasYInactive;
    private Button.OnPress action = (button) -> {};
    private Component text = TextComponent.EMPTY;

    Builder(int x, int y, int width, int height,
        ResourceLocation textureLocation, Function<Builder, CompositeButton> factory) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.textureLocation = textureLocation;
      this.factory = factory;
    }

    public Builder setAtlasPos(int x, int y) {
      this.atlasX = x;
      this.atlasY = y;
      return this;
    }

    public Builder setHoverAtlasPos(int x, int y) {
      this.atlasXHover = x;
      this.atlasYHover = y;
      return this;
    }

    public Builder setInactiveAtlasPos(int x, int y) {
      this.atlasXInactive = x;
      this.atlasYInactive = y;
      return this;
    }

    public Builder setAction(Button.OnPress action) {
      this.action = action;
      return this;
    }

    public Builder setText(Component text) {
      this.text = text;
      return this;
    }

    public CompositeButton build() {
      return factory.apply(this);
    }
  }
}
