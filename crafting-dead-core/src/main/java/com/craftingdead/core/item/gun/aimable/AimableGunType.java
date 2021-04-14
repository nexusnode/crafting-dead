package com.craftingdead.core.item.gun.aimable;

import java.util.function.Function;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.SerializableCapabilityProvider;
import com.craftingdead.core.item.gun.AbstractGunType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class AimableGunType extends AbstractGunType<AimableGun> {

  /**
   * Whether the gun has bolt action
   */
  private final boolean boltAction;

  protected AimableGunType(Builder builder) {
    super(builder);
    this.boltAction = builder.boltAction;
  }

  @Override
  public Function<AimableGun, AimableGunClient> getClientFactory() {
    return AimableGunClient::new;
  }

  @Override
  public ICapabilityProvider createCapabilityProvider(ItemStack itemStack) {
    return new SerializableCapabilityProvider<>(
        LazyOptional.of(() -> new AimableGun(this, itemStack)),
        ImmutableSet.of(() -> ModCapabilities.GUN, () -> ModCapabilities.COMBAT_SLOT_PROVIDER,
            () -> ModCapabilities.ANIMATION_PROVIDER, () -> ModCapabilities.SCOPE),
        CompoundNBT::new);
  }

  public boolean hasBoltAction() {
    return this.boltAction;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder extends AbstractGunType.Builder<AimableGun, Builder> {

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
