package com.craftingdead.immerse.client.shader;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;

public record RectShader(ShaderInstance instance, Uniform radius, Uniform position, Uniform size) {

  public RectShader(ShaderInstance instance) {
    this(instance,
        instance.getUniform("u_Radius"),
        instance.getUniform("u_Position"),
        instance.getUniform("u_Size"));
  }
}
