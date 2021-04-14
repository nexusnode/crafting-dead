package com.craftingdead.core.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class EmptyStorage<C> implements Capability.IStorage<C> {

  private static final EmptyStorage<? extends Object> INSTANCE = new EmptyStorage<>();

  @SuppressWarnings("unchecked")
  public static <C> EmptyStorage<C> getInstance() {
    return (EmptyStorage<C>) INSTANCE;
  }

  @Override
  public INBT writeNBT(Capability<C> capability, C instance, Direction side) {
    return null;
  }

  @Override
  public void readNBT(Capability<C> capability, C instance, Direction side, INBT nbt) {}
}
