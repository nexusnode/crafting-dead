package com.craftingdead.core.world.item.gun.aimable;

import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.GunItem;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.gun.Gun;
import com.craftingdead.core.world.item.gun.GunConfiguration;
import com.craftingdead.core.world.item.scope.Scope;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class AimableGunItem extends GunItem {

  protected AimableGunItem(Builder<?> builder) {
    super(builder);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, CompoundTag nbt) {
    return CapabilityUtil.serializableProvider(() -> new AimableGun(itemStack, this),
        Gun.CAPABILITY, CombatSlotProvider.CAPABILITY, Scope.CAPABILITY);
  }

  public static Builder<?> builder(ResourceKey<GunConfiguration> propertiesKey) {
    return new Builder<>(AimableGunItem::new, propertiesKey);
  }
}
