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

public record FlashParticleOptions(float red, float green, float blue, float scale)
    implements ParticleOptions {

  public static final Codec<FlashParticleOptions> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Codec.FLOAT.fieldOf("red").forGetter(FlashParticleOptions::red),
              Codec.FLOAT.fieldOf("green").forGetter(FlashParticleOptions::green),
              Codec.FLOAT.fieldOf("blue").forGetter(FlashParticleOptions::blue),
              Codec.FLOAT.fieldOf("scale").forGetter(FlashParticleOptions::scale))
          .apply(instance, FlashParticleOptions::new));

  @SuppressWarnings("deprecation")
  public static final ParticleOptions.Deserializer<FlashParticleOptions> DESERIALIZER =
      new ParticleOptions.Deserializer<FlashParticleOptions>() {
        @Override
        public FlashParticleOptions fromCommand(ParticleType<FlashParticleOptions> particleType,
            StringReader stringReader) throws CommandSyntaxException {
          stringReader.expect(' ');
          float red = stringReader.readFloat();
          stringReader.expect(' ');
          float green = stringReader.readFloat();
          stringReader.expect(' ');
          float blue = stringReader.readFloat();
          stringReader.expect(' ');
          float scale = stringReader.readFloat();
          return new FlashParticleOptions(red, green, blue, scale);
        }

        @Override
        public FlashParticleOptions fromNetwork(ParticleType<FlashParticleOptions> particleType,
            FriendlyByteBuf packetBuffer) {
          return new FlashParticleOptions(packetBuffer.readFloat(), packetBuffer.readFloat(),
              packetBuffer.readFloat(), packetBuffer.readFloat());
        }
      };

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
  public ParticleType<FlashParticleOptions> getType() {
    return ModParticleTypes.RGB_FLASH.get();
  }
}
