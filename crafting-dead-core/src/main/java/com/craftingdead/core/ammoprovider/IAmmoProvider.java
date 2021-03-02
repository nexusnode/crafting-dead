package com.craftingdead.core.ammoprovider;

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.capability.magazine.IMagazine;
import com.craftingdead.core.util.IBufferSerializable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IAmmoProvider extends INBTSerializable<CompoundNBT>, IBufferSerializable {

  void reload(ILiving<?, ?> living);

  void unload(ILiving<?, ?> living);

  int getReserveSize();

  ItemStack getMagazineStack();

  default IMagazine getExpectedMagazine() {
    return this.getMagazine()
        .orElseThrow(() -> new IllegalStateException("No magazine capability"));
  }

  default LazyOptional<IMagazine> getMagazine() {
    return this.getMagazineStack().getCapability(ModCapabilities.MAGAZINE);
  }

  AmmoProviderType getType();
}
