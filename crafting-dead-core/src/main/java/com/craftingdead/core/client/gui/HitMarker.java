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
package com.craftingdead.core.client.gui;

import java.util.Optional;
import java.util.function.BiFunction;
import org.lwjgl.opengl.GL11;
import com.craftingdead.core.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HitMarker {

  private static final int HIT_MARKER_FADE_TIME_MS = 200;
  private static final int HIT_MARKER_SIZE = 12;

  private final Vec3d pos;
  private final Type kill;

  private long fadeStartTimeMs;

  /**
   * To create a {@link HitMarker} use {@link Mode#createHitMarker(Vec3d, boolean)}.
   * 
   * @param pos
   * @param kill
   */
  private HitMarker(Vec3d pos, Type kill) {
    this.pos = pos;
    this.kill = kill;
  }

  /**
   * Render the hit marker onto the screen.
   * 
   * @param width - screen width
   * @param height - screen height
   * @param partialTicks
   * @return if the hit marker has fully faded
   */
  public boolean render(int width, int height, float partialTicks) {
    if (this.fadeStartTimeMs == 0L) {
      this.fadeStartTimeMs = Util.milliTime();
    }
    float zeroToOneFadePct = MathHelper.clamp(
        (float) (Util.milliTime() - this.fadeStartTimeMs) / HIT_MARKER_FADE_TIME_MS,
        0.0F, 1.0F);
    final float oneToZeroFadePct = 1.0F - zeroToOneFadePct;

    if (zeroToOneFadePct == 1.0F) {
      return true;
    }

    RenderUtil.projectToPlayerView(this.pos.getX(), this.pos.getY(), this.pos.getZ(), partialTicks)
        .ifPresent(pos -> {
          float alpha = (float) (this.kill.colour >> 24 & 255) / 255.0F;
          float red = (float) (this.kill.colour >> 16 & 255) / 255.0F;
          float green = (float) (this.kill.colour >> 8 & 255) / 255.0F;
          float blue = (float) (this.kill.colour & 255) / 255.0F;

          RenderSystem.enableBlend();
          RenderSystem.color4f(red, green, blue, alpha * oneToZeroFadePct);
          RenderSystem.pushMatrix();
          {
            // To draw a cross, it is needed only two values: the least and the higher positions
            float leastCrossEndPos = HIT_MARKER_SIZE * oneToZeroFadePct;
            float higherCrossEndPos = leastCrossEndPos * 2;
            // Mean, useful to centralize
            float markerSizeMean = (higherCrossEndPos + leastCrossEndPos) / 2F;

            RenderSystem.translatef((width / 2) + pos.x - markerSizeMean,
                (height / 2) - pos.y - markerSizeMean, 0);
            RenderSystem.lineWidth(oneToZeroFadePct * 4.5F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(higherCrossEndPos, leastCrossEndPos, 0.0D).endVertex();
            bufferbuilder.pos(leastCrossEndPos, higherCrossEndPos, 0.0D).endVertex();
            tessellator.draw();
            bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(leastCrossEndPos, leastCrossEndPos, 0.0D).endVertex();
            bufferbuilder.pos(higherCrossEndPos, higherCrossEndPos, 0.0D).endVertex();
            tessellator.draw();
          }
          RenderSystem.popMatrix();
          RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
          RenderSystem.disableBlend();
        });
    return false;
  }

  public static enum Type {
    HIT(0xFFFFFFFF), KILL(0xFFB30C00);

    private final int colour;

    private Type(int colour) {
      this.colour = colour;
    }
  }

  public static enum Mode {
    OFF((pos, kill) -> Optional.empty()), KILL(
        (pos, kill) -> kill ? Optional.of(new HitMarker(pos, Type.KILL))
            : Optional.empty()), HIT_AND_KILL(
                (pos, kill) -> Optional.of(new HitMarker(pos, kill ? Type.KILL : Type.HIT)));

    private final BiFunction<Vec3d, Boolean, Optional<HitMarker>> factory;

    private Mode(BiFunction<Vec3d, Boolean, Optional<HitMarker>> factory) {
      this.factory = factory;
    }

    public Optional<HitMarker> createHitMarker(Vec3d pos, boolean kill) {
      return this.factory.apply(pos, kill);
    }
  }
}
