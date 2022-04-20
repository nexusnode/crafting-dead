package com.craftingdead.core.world.item.gun;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collections;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public record GunProperties(
    Attributes attributes,
    Sounds sounds) {

  public static final Codec<GunProperties> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Attributes.CODEC
                  .fieldOf("attributes")
                  .forGetter(t -> t.attributes),
              Sounds.CODEC
                  .fieldOf("sounds")
                  .forGetter(t -> t.sounds))
          .apply(instance, GunProperties::new));

  public record Attributes(
      int fireDelay,
      double damage,
      int reloadDuration,
      double accuracy,
      double recoil,
      int roundsPerShot,
      double range,
      boolean hasCrossHair,
      List<FireMode> fireModes,
      List<ResourceLocation> acceptedMagazines,
      ResourceLocation defaultMagazine,
      List<ResourceLocation> acceptedAttachments) {
    public static final Codec<Attributes> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.INT.optionalFieldOf("fire_delay", 0)
                    .forGetter(t -> t.fireDelay),
                Codec.DOUBLE.optionalFieldOf("damage", 1D)
                    .forGetter(t -> t.damage),
                Codec.INT.optionalFieldOf("reload_duration", 1)
                    .forGetter(t -> t.reloadDuration),
                Codec.DOUBLE.optionalFieldOf("accuracy", 1D)
                    .forGetter(t -> t.accuracy),
                Codec.DOUBLE.optionalFieldOf("recoil", 0D)
                    .forGetter(t -> t.recoil),
                Codec.INT.optionalFieldOf("rounds_per_shot", 1)
                    .forGetter(t -> t.roundsPerShot),
                Codec.DOUBLE.optionalFieldOf("range", 10D)
                    .forGetter(t -> t.range),
                Codec.BOOL.optionalFieldOf("has_crosshair", true)
                    .forGetter(t -> t.hasCrossHair),
                Codec.list(FireMode.CODEC)
                    .optionalFieldOf("fire_mode", Collections.singletonList(FireMode.SEMI))
                    .forGetter(t -> t.fireModes),
                Codec.list(ResourceLocation.CODEC)
                    .optionalFieldOf("accepted_magazines", Collections.emptyList())
                    .forGetter(t -> t.acceptedMagazines),
                ResourceLocation.CODEC.fieldOf("default_magazine")
                    .forGetter(t -> t.defaultMagazine),
                Codec.list(ResourceLocation.CODEC)
                    .optionalFieldOf("accepted_attachments", Collections.emptyList())
                    .forGetter(t -> t.acceptedAttachments)
                )
            .apply(instance, Attributes::new));

  }

  public record Sounds(
      ResourceLocation shootSound,
      ResourceLocation distantShootSound,
      ResourceLocation silencedShootSound,
      ResourceLocation reloadSound) {
    public static final Codec<Sounds> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(
                ResourceLocation.CODEC
                    .optionalFieldOf("shoot_sound", null)
                    .forGetter(t -> t.shootSound),
                ResourceLocation.CODEC
                    .optionalFieldOf("distant_shoot_sound", null)
                    .forGetter(t -> t.distantShootSound),
                ResourceLocation.CODEC
                    .optionalFieldOf("silenced_shoot_sound", null)
                    .forGetter(t -> t.silencedShootSound),
                ResourceLocation.CODEC
                    .optionalFieldOf("reload_sound", null)
                    .forGetter(t -> t.reloadSound))
            .apply(instance, Sounds::new));

  }
}
