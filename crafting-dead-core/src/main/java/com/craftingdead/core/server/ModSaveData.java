package com.craftingdead.core.server;

import com.craftingdead.core.CraftingDead;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class ModSaveData extends WorldSavedData {
  
  private Logical

  public ModSaveData() {
    super(CraftingDead.ID);
  }

  @Override
  public void read(CompoundNBT nbt) {

  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    return null;
  }
}
