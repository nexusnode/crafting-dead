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

package com.craftingdead.core.particle;

import java.util.Locale;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;

public class RGBFlashParticleData implements IParticleData {

  public static final Codec<RGBFlashParticleData> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(Codec.FLOAT.fieldOf("red").forGetter(RGBFlashParticleData::getRed),
              Codec.FLOAT.fieldOf("green").forGetter(RGBFlashParticleData::getGreen),
              Codec.FLOAT.fieldOf("blue").forGetter(RGBFlashParticleData::getBlue),
              Codec.FLOAT.fieldOf("scale").forGetter(RGBFlashParticleData::getScale))
          .apply(instance, RGBFlashParticleData::new));

  @SuppressWarnings("deprecation")
  public static final IParticleData.IDeserializer<RGBFlashParticleData> DESERIALIZER =
      new IParticleData.IDeserializer<RGBFlashParticleData>() {
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
            PacketBuffer packetBuffer) {
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
  public void writeToNetwork(PacketBuffer packetBuffer) {
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
