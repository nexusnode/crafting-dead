/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.particle;

import java.util.Locale;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class RGBFlashParticleData implements ParticleOptions {

  public static final Codec<RGBFlashParticleData> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(Codec.FLOAT.fieldOf("red").forGetter(RGBFlashParticleData::getRed),
              Codec.FLOAT.fieldOf("green").forGetter(RGBFlashParticleData::getGreen),
              Codec.FLOAT.fieldOf("blue").forGetter(RGBFlashParticleData::getBlue),
              Codec.FLOAT.fieldOf("scale").forGetter(RGBFlashParticleData::getScale))
          .apply(instance, RGBFlashParticleData::new));

  @SuppressWarnings("deprecation")
  public static final ParticleOptions.Deserializer<RGBFlashParticleData> DESERIALIZER =
      new ParticleOptions.Deserializer<RGBFlashParticleData>() {
        @Override
        public RGBFlashParticleData fromCommand(ParticleType<RGBFlashParticleData> particleType,
            StringReader stringReader) throws CommandSyntaxException {
          stringReader.expect(' ');
          float red = stringReader.readFloat();
          stringReader.expect(' ');
          float green = stringReader.readFloat();
          stringReader.expect(' ');
          float blue = stringReader.readFloat();
          stringReader.expect(' ');
          float scale = stringReader.readFloat();
          return new RGBFlashParticleData(red, green, blue, scale);
        }

        @Override
        public RGBFlashParticleData fromNetwork(ParticleType<RGBFlashParticleData> particleType,
            FriendlyByteBuf packetBuffer) {
          return new RGBFlashParticleData(packetBuffer.readFloat(), packetBuffer.readFloat(),
              packetBuffer.readFloat(), packetBuffer.readFloat());
        }
      };

  private final float red;
  private final float green;
  private final float blue;
  private final float scale;

  public RGBFlashParticleData(float red, float green, float blue, float scale) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.scale = scale;
  }

  @Override
  public void writeToNetwork(FriendlyByteBuf packetBuffer) {
    packetBuffer.writeFloat(this.red);
    packetBuffer.writeFloat(this.green);
    packetBuffer.writeFloat(this.blue);
    packetBuffer.writeFloat(this.scale);
  }

  @Override
  public String writeToString() {
    return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f",
        this.getType().getRegistryName(), this.red, this.green, this.blue, this.scale);
  }

  @Override
  public ParticleType<RGBFlashParticleData> getType() {
    return ModParticleTypes.RGB_FLASH.get();
  }

  public float getRed() {
    return this.red;
  }

  public float getGreen() {
    return this.green;
  }

  public float getBlue() {
    return this.blue;
  }

  public float getScale() {
    return this.scale;
  }
}
