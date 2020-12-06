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
package com.craftingdead.immerse.client.shader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.craftingdead.immerse.CraftingDeadImmerse;
import net.minecraft.client.shader.IShaderManager;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class ShaderProgram implements IShaderManager {

  public static final Logger logger = LogManager.getLogger();

  private int program;

  private ShaderLoader vertexShader;
  private ShaderLoader fragmentShader;

  @Nonnull
  private final ResourceLocation vertexShaderLocation;
  @Nonnull
  private final ResourceLocation fragmentShaderLocation;

  public ShaderProgram(@Nonnull ResourceLocation vert, @Nonnull ResourceLocation frag) {
    this.vertexShaderLocation = vert;
    this.fragmentShaderLocation = frag;
  }

  public ShaderProgram(@Nonnull String vert, @Nonnull String frag) {
    this(new ResourceLocation(CraftingDeadImmerse.ID, String.format("shaders/%s.vert", vert)),
        new ResourceLocation(CraftingDeadImmerse.ID, String.format("shaders/%s.frag", frag)));
  }

  public void compile(IResourceManager manager) {
    if (this.vertexShader != null || this.fragmentShader != null) {
      ShaderLinkHelper.deleteShader(this);
    }
    try {
      this.vertexShader =
          createShader(manager, this.vertexShaderLocation, ShaderLoader.ShaderType.VERTEX);
      this.fragmentShader =
          createShader(manager, this.fragmentShaderLocation, ShaderLoader.ShaderType.FRAGMENT);
      this.program = ShaderLinkHelper.createProgram();
      ShaderLinkHelper.linkProgram(this);
    } catch (IOException e) {
      logger.fatal("Can't create program {}", getClass().getSimpleName(), e);
    }
  }

  @Nonnull
  private static ShaderLoader createShader(IResourceManager manager,
      @Nonnull ResourceLocation location, ShaderLoader.ShaderType type) throws IOException {
    IResource resource = manager.getResource(location);
    try (InputStream stream = new BufferedInputStream(resource.getInputStream())) {
      return ShaderLoader.func_216534_a(type, location.toString(), stream, resource.getPackName());
    }
  }

  @Override
  public int getProgram() {
    return this.program;
  }

  @Override
  public void markDirty() {}

  @Nonnull
  @Override
  public ShaderLoader getVertexShaderLoader() {
    return this.vertexShader;
  }

  @Nonnull
  @Override
  public ShaderLoader getFragmentShaderLoader() {
    return this.fragmentShader;
  }
}
