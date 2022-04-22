/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item.gun;

import com.craftingdead.core.world.item.gun.attachment.Attachment;
import com.craftingdead.core.world.item.gun.attachment.Attachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public record GunProperties(
    GunProperties.Attributes attributes,
    GunProperties.Sounds sounds,
    GunProperties.AimableGunAttributes aimableGunAttributes) {

  public static final Codec<GunProperties> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Attributes.CODEC
                  .fieldOf("attributes")
                  .forGetter(GunProperties::attributes),
              Sounds.CODEC
                  .fieldOf("sounds")
                  .forGetter(GunProperties::sounds),
              AimableGunAttributes.CODEC
                  .optionalFieldOf("aimable_gun_attributes", null)
                  .forGetter(GunProperties::aimableGunAttributes))
          .apply(instance, GunProperties::new));

  /**
   * @param fireDelay           - Time between shots in milliseconds.
   * @param damage              - Damage inflicted by a single shot from this gun.
   * @param reloadDuration      - The duration of time this gun takes to reload in ticks.
   * @param accuracy            - Accuracy as percentage.
   * @param recoil              - Recoil.
   * @param roundsPerShot       - Amount of rounds to be fired in a single shot. e.g. for shotguns
   * @param range               - Range in blocks.
   * @param hasCrossHair        - Whether the crosshair should be rendered or not while holding this
   *                            item.
   * @param fireModes           - {@link FireMode}s the gun can cycle through.
   * @param acceptedMagazines   - A set of magazines that are supported by this gun.
   * @param defaultMagazine     - The default magazine that is supplied with this gun when crafted.
   * @param acceptedAttachments - A set of attachments that are supported by this gun.
   */
  public record Attributes(
      int fireDelay,
      int damage,
      int reloadDuration,
      float accuracy,
      float recoil,
      int roundsPerShot,
      double range,
      boolean hasCrossHair,
      Set<FireMode> fireModes,
      Set<Supplier<Item>> acceptedMagazines,
      Supplier<Item> defaultMagazine,
      Set<Supplier<Attachment>> acceptedAttachments) {

    public static final Codec<Attributes> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(
                Codec.INT.optionalFieldOf("fire_delay", 0)
                    .forGetter(Attributes::fireDelay),
                Codec.INT.optionalFieldOf("damage", 1)
                    .forGetter(Attributes::damage),
                Codec.INT.optionalFieldOf("reload_duration", 1)
                    .forGetter(Attributes::reloadDuration),
                Codec.FLOAT.optionalFieldOf("accuracy", 1F)
                    .forGetter(Attributes::accuracy),
                Codec.FLOAT.optionalFieldOf("recoil", 0F)
                    .forGetter(Attributes::recoil),
                Codec.INT.optionalFieldOf("rounds_per_shot", 1)
                    .forGetter(Attributes::roundsPerShot),
                Codec.DOUBLE.optionalFieldOf("range", 10D)
                    .forGetter(Attributes::range),
                Codec.BOOL.optionalFieldOf("has_crosshair", true)
                    .forGetter(Attributes::hasCrossHair),
                Codec.list(FireMode.CODEC)
                    .optionalFieldOf("fire_mode", Collections.singletonList(FireMode.SEMI))
                    .xmap(to -> (Set<FireMode>) new HashSet<>(to), ArrayList::new)
                    .forGetter(Attributes::fireModes),
                Codec.list(ForgeRegistries.ITEMS.getCodec()
                        .xmap(to -> (Supplier<Item>) () -> to, Supplier::get))
                    .optionalFieldOf("accepted_magazines", Collections.emptyList())
                    .xmap(to -> (Set<Supplier<Item>>) new HashSet<>(to), ArrayList::new)
                    .forGetter(Attributes::acceptedMagazines),
                ForgeRegistries.ITEMS.getCodec().fieldOf("default_magazine")
                    .xmap(to -> (Supplier<Item>) () -> to, Supplier::get)
                    .forGetter(t -> t.defaultMagazine),
                Codec.list(Attachments.REGISTRY.get()
                        .getCodec().xmap(to -> (Supplier<Attachment>) () -> to, Supplier::get))
                    .optionalFieldOf("accepted_attachments", Collections.emptyList())
                    .xmap(to -> (Set<Supplier<Attachment>>) new HashSet<>(to), ArrayList::new)
                    .forGetter(Attributes::acceptedAttachments)
            )
            .apply(instance, Attributes::new));
  }

  /**
   * @param shootSound         - Sound to play for each shot of the gun.
   * @param distantShootSound  - Sound to play for each shot of the gun when far away.
   * @param silencedShootSound - A 'silenced' version of the shoot sound.
   * @param reloadSound        - Sound to play whilst the gun is being reloaded.
   */
  public record Sounds(
      Supplier<SoundEvent> shootSound,
      Supplier<SoundEvent> distantShootSound,
      Supplier<SoundEvent> silencedShootSound,
      Supplier<SoundEvent> reloadSound) {

    public static final Codec<Sounds> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .fieldOf("shoot_sound")
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::shootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("distant_shoot_sound", null)
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::distantShootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("silenced_shoot_sound", null)
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::silencedShootSound),
                ForgeRegistries.SOUND_EVENTS.getCodec()
                    .optionalFieldOf("reload_sound", null)
                    .xmap(to -> (Supplier<SoundEvent>) () -> to, Supplier::get)
                    .forGetter(Sounds::reloadSound))
            .apply(instance, Sounds::new));
  }

  //TODO: I don't know an easier way to extend gun attributes for sub gun types,
  // so I added them here - juanmuscaria
  /**
   * @param boltAction Whether the gun has bolt action
   */
  public record AimableGunAttributes(boolean boltAction) {
    public static final Codec<AimableGunAttributes> CODEC =
        RecordCodecBuilder.create(instance -> instance
            .group(Codec.BOOL
                .optionalFieldOf("bolt_action", false)
                .forGetter(AimableGunAttributes::boltAction))
            .apply(instance, AimableGunAttributes::new));
  }
}
