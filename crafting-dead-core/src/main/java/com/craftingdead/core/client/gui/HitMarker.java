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

package com.craftingdead.core.client.gui;

import java.util.Optional;
import java.util.function.BiFunction;
import com.craftingdead.core.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class HitMarker {

  private static final int HIT_MARKER_FADE_TIME_MS = 200;
  private static final int HIT_MARKER_SIZE = 12;

  private final Vec3 pos;
  private final Type type;

  private long fadeStartTimeMs;

  /**
   * To create a {@link HitMarker} use {@link Mode#createHitMarker(Vec3d, boolean)}.
   * 
   * @param pos
   * @param type
   */
  private HitMarker(Vec3 pos, Type type) {
    this.pos = pos;
    this.type = type;
  }

  /**
   * Render the hit marker onto the screen.
   * 
   * @param width - screen width
   * @param height - screen height
   * @param partialTicks
   * @return if the hit marker has fully faded
   */
  public boolean render(PoseStack poseStack, int width, int height, float partialTicks) {
    if (this.fadeStartTimeMs == 0L) {
      this.fadeStartTimeMs = Util.getMillis();
    }
    final var zeroToOneFadePct = Mth.clamp(
        (float) (Util.getMillis() - this.fadeStartTimeMs) / HIT_MARKER_FADE_TIME_MS,
        0.0F, 1.0F);
    final var oneToZeroFadePct = 1.0F - zeroToOneFadePct;

    if (zeroToOneFadePct == 1.0F) {
      return true;
    }

    final var pos =
        RenderUtil.projectToPlayerView(this.pos.x(), this.pos.y(), this.pos.z(), partialTicks);
    if (pos == null) {
      return false;
    }

    final var alpha = (this.type.colour >> 24 & 255) / 255.0F;
    final var red = (this.type.colour >> 16 & 255) / 255.0F;
    final var green = (this.type.colour >> 8 & 255) / 255.0F;
    final var blue = (this.type.colour & 255) / 255.0F;

    RenderSystem.enableBlend();
    RenderSystem.setShaderColor(red, green, blue, alpha * oneToZeroFadePct);
    RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
    poseStack.pushPose();
    {
      // To draw a cross, it is needed only two values: the least and the higher positions
      final var leastCrossEndPos = HIT_MARKER_SIZE * oneToZeroFadePct;
      final var higherCrossEndPos = leastCrossEndPos * 2;
      // Mean, useful to centralize
      final var markerSizeMean = (higherCrossEndPos + leastCrossEndPos) / 2F;

      poseStack.translate((width / 2) + pos.x - markerSizeMean,
          (height / 2) - pos.y - markerSizeMean, 0);
      RenderSystem.lineWidth(oneToZeroFadePct * 4.5F);
      final var tessellator = Tesselator.getInstance();
      final var builder = tessellator.getBuilder();
      final var matrix = poseStack.last().pose();
      builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
      builder
          .vertex(matrix, higherCrossEndPos, leastCrossEndPos, 0.0F)
          .color(255, 255, 255, 255)
          .normal(0.0F, 1.0F, 0.0F)
          .endVertex();
      builder
          .vertex(matrix, leastCrossEndPos, higherCrossEndPos, 0.0F)
          .color(255, 255, 255, 255)
          .normal(0.0F, 1.0F, 0.0F)
          .endVertex();
      tessellator.end();
      builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
      builder
          .vertex(matrix, leastCrossEndPos, leastCrossEndPos, 0.0F)
          .color(255, 255, 255, 255)
          .normal(1.0F, 0.0F, 0.0F).endVertex();
      builder
          .vertex(matrix, higherCrossEndPos, higherCrossEndPos, 0.0F)
          .color(255, 255, 255, 255)
          .normal(1.0F, 0.0F, 0.0F).endVertex();
      tessellator.end();
    }
    poseStack.popPose();
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();

    return false;
  }

  public enum Type {

    HIT(0xFFFFFFFF), KILL(0xFFB30C00);

    private final int colour;

    private Type(int colour) {
      this.colour = colour;
    }
  }

  public enum Mode {

    OFF((pos, kill) -> Optional.empty()),
    KILL((pos, kill) -> kill
        ? Optional.of(new HitMarker(pos, Type.KILL))
        : Optional.empty()),
    HIT_AND_KILL((pos, kill) -> Optional.of(new HitMarker(pos, kill ? Type.KILL : Type.HIT)));

    private final BiFunction<Vec3, Boolean, Optional<HitMarker>> factory;

    private Mode(BiFunction<Vec3, Boolean, Optional<HitMarker>> factory) {
      this.factory = factory;
    }

    public Optional<HitMarker> createHitMarker(Vec3 pos, boolean kill) {
      return this.factory.apply(pos, kill);
    }
  }
}
