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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.TextureUtil;

public class Shader {

  private final Shader.Type type;
  private final String name;
  private final int id;
  private int references;

  private Shader(Shader.Type type, int id, String name) {
    this.type = type;
    this.id = id;
    this.name = name;
  }

  public void attach(int programId) {
    RenderSystem.assertThread(RenderSystem::isOnRenderThread);
    ++this.references;
    GlStateManager.glAttachShader(programId, this.id);
  }

  public void close() {
    RenderSystem.assertThread(RenderSystem::isOnRenderThread);
    --this.references;
    if (this.references <= 0) {
      GlStateManager.glDeleteShader(this.id);
      this.type.getPrograms().remove(this.name);
    }
  }

  public String getName() {
    return this.name;
  }

  public static Shader compile(Type type, String name, InputStream inputStream)
      throws IOException {
    RenderSystem.assertThread(RenderSystem::isOnRenderThread);
    String source = TextureUtil.readResourceAsString(inputStream);
    if (source == null) {
      throw new IOException("Could not load program " + type.getName());
    }

    int id = GlStateManager.glCreateShader(type.getGlType());
    GlStateManager.glShaderSource(id, source);
    GlStateManager.glCompileShader(id);
    if (GlStateManager.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0) {
      String info = StringUtils.trim(GlStateManager.glGetShaderInfoLog(id, 0x8000));
      throw new IOException(
          "Couldn't compile " + type.getName() + " program (" + name + ") : " + info);
    } else {
      Shader shader = new Shader(type, id, name);
      type.getPrograms().put(name, shader);
      return shader;
    }
  }

  public static enum Type {

    VERTEX("vertex", ".vsh", GL20.GL_VERTEX_SHADER),
    FRAGMENT("fragment", ".fsh", GL20.GL_FRAGMENT_SHADER),
    TESS_EVALUATION("vertex", ".tes", GL40.GL_TESS_EVALUATION_SHADER),
    TESS_CONTROL("fragment", ".tcs", GL40.GL_TESS_CONTROL_SHADER);

    private final String name;
    private final String extension;
    private final int glType;
    private final Map<String, Shader> programs = Maps.newHashMap();

    private Type(String name, String extension, int glType) {
      this.name = name;
      this.extension = extension;
      this.glType = glType;
    }

    public String getName() {
      return this.name;
    }

    public String getExtension() {
      return this.extension;
    }

    private int getGlType() {
      return this.glType;
    }

    public Map<String, Shader> getPrograms() {
      return this.programs;
    }
  }
}
