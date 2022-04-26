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

import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunType;
import com.craftingdead.core.world.item.gun.GunTypeFactories;
import com.craftingdead.core.world.item.gun.GunTypeFactory;
import com.craftingdead.core.world.item.scope.Scope;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class AimableGunType extends GunType {

  public static final Codec<AimableGunType> DIRECT_CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              GeneralAttributes.CODEC
                  .fieldOf("general_attributes")
                  .forGetter(AimableGunType::getAttributes),
              Sounds.CODEC
                  .fieldOf("sounds")
                  .forGetter(AimableGunType::getSounds),
              AimAttributes.CODEC
                  .optionalFieldOf("aim_attributes", new AimAttributes(false))
                  .forGetter(AimableGunType::getAimAttributes))
          .apply(instance, AimableGunType::new));

  private final AimAttributes aimAttributes;

  private AimableGunType(GeneralAttributes attributes, Sounds sounds, AimAttributes aimAttributes) {
    super(attributes, sounds);
    this.aimAttributes = aimAttributes;
  }

  protected AimableGunType(Builder builder) {
    super(builder);
    this.aimAttributes = new AimAttributes(builder.boltAction);
  }

  public AimAttributes getAimAttributes() {
    return this.aimAttributes;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, CompoundTag nbt) {
    return CapabilityUtil.serializableProvider(
        () -> AimableGun.create(AimableGunClient::new, itemStack, this),
        Gun.CAPABILITY, CombatSlotProvider.CAPABILITY, Scope.CAPABILITY);
  }

  public boolean hasBoltAction() {
    return this.aimAttributes.boltAction();
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public GunTypeFactory getFactory() {
    return GunTypeFactories.AIMABLE.get();
  }

  public static class Builder extends GunType.Builder<Builder> {

    private boolean boltAction = false;

    private Builder() {
      super(AimableGunType::new);
    }

    public Builder setBoltAction(boolean boltAction) {
      this.boltAction = boltAction;
      return this;
    }
  }
}
