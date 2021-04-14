package com.craftingdead.core.item.gun.simple;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.item.gun.AbstractGunType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class SimpleGunType extends AbstractGunType<SimpleGun> {

  protected SimpleGunType(Builder<SimpleGun, ?> builder) {
    super(builder);
  }

  @Override
  public ICapabilityProvider createCapabilityProvider(ItemStack itemStack) {
    return new SerializableCapabilityProvider<>(
        LazyOptional.of(() -> new SimpleGun(this, itemStack)),
        ImmutableSet.of(() -> ModCapabilities.GUN, () -> ModCapabilities.COMBAT_SLOT_PROVIDER,
            () -> ModCapabilities.ANIMATION_PROVIDER),
        CompoundNBT::new);

  }

  public static Builder<SimpleGun, ?> builder() {
    return new Builder<>(SimpleGunType::new);
  }
}
