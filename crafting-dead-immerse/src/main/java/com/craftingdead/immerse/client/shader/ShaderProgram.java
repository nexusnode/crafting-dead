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

package com.craftingdead.immerse.client.shader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL20;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ShaderProgram {

  public static final Logger logger = LogManager.getLogger();

  private final Map<Shader.Type, ResourceLocation> shaderLocations;

  private final String name;

  private int programId;

  private Set<Shader> shaders = Collections.emptySet();

  public ShaderProgram(String name, Map<Shader.Type, ResourceLocation> shaderLocations) {
    this.name = name;
    this.shaderLocations = new EnumMap<>(shaderLocations);
  }

  public void use() {
    RenderSystem.assertThread(RenderSystem::isOnRenderThread);
    ShaderLinkHelper.glUseProgram(this.programId);
  }

  public void link() throws IOException {
    RenderSystem.assertThread(RenderSystem::isOnRenderThread);
    this.shaders.forEach(shader -> shader.attach(this.programId));
    GlStateManager.glLinkProgram(this.programId);
    if (GlStateManager.glGetProgrami(this.programId, GL20.GL_LINK_STATUS) == 0) {
      logger.warn("Failed to link program {}:", this.name);
      logger.warn(GlStateManager.glGetProgramInfoLog(this.programId, 0x8000));
    }
  }

  public void release() {
    RenderSystem.assertThread(RenderSystem::isOnRenderThread);
    this.shaders.forEach(Shader::close);
    this.shaders.clear();
    GlStateManager.glDeleteProgram(this.programId);
  }

  public void compile(IResourceManager resourceManager) {
    if (!this.shaders.isEmpty()) {
      this.release();
    }
    try {
      this.shaders = this.shaderLocations.entrySet().stream()
          .map(entry -> createShader(resourceManager, entry.getKey(), entry.getValue()))
          .filter(Objects::nonNull)
          .collect(Collectors.toSet());
      this.programId = ShaderLinkHelper.createProgram();
      this.link();
    } catch (IOException e) {
      logger.fatal("Failed to create program {}: ", this.name, e);
    }
  }

  @Nullable
  private static Shader createShader(IResourceManager resourceManager, Shader.Type type,
      ResourceLocation location) {
    try (InputStream stream =
        new BufferedInputStream(resourceManager.getResource(location).getInputStream())) {
      return Shader.compile(type, location.toString(), stream);
    } catch (IOException e) {
      logger.warn("Failed to load shader {}: ", location.toString(), e);
      return null;
    }
  }
}
