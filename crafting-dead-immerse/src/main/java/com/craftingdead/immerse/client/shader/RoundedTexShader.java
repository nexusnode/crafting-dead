package com.craftingdead.immerse.client.shader;

import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;

public record RoundedTexShader(ShaderInstance instance, Uniform radius, Uniform position,
    Uniform size, Uniform outlineWidth, Uniform outlineColor) {

  public RoundedTexShader(ShaderInstance instance) {
    this(instance,
        instance.getUniform("u_Radius"),
        instance.getUniform("u_Position"),
        instance.getUniform("u_Size"),
        instance.getUniform("u_OutlineWidth"),
        instance.getUniform("u_OutlineColor"));
  }
}
