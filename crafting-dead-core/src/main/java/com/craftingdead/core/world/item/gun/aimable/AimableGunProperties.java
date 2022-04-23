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

package com.craftingdead.core.world.item.gun.aimable;

import com.craftingdead.core.world.item.gun.GunProperties;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;

public class AimableGunProperties extends GunProperties {

  private final AimableGunAttributes aimableGunAttributes;

  public AimableGunProperties(Attributes attributes,
      Sounds sounds, AimableGunAttributes aimableGunAttributes) {
    super(attributes, sounds);
    this.aimableGunAttributes = aimableGunAttributes;
  }

  public AimableGunAttributes aimableGunAttributes() {
    return aimableGunAttributes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    AimableGunProperties that = (AimableGunProperties) o;
    return aimableGunAttributes.equals(that.aimableGunAttributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), aimableGunAttributes);
  }

  /**
   * @param boltAction - Whether the gun has bolt action
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
