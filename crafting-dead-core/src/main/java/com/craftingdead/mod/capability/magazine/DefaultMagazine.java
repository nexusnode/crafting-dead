package com.craftingdead.mod.capability.magazine;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;

public class DefaultMagazine implements IMagazine {

  @Override
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {}

  @Override
  public void hitEntity(ItemStack magazineStack, Entity entity, EntityRayTraceResult rayTrace) {}

  @Override
  public void hitBlock(ItemStack magazineStack, Entity entity, BlockRayTraceResult rayTrace) {}

  @Override
  public float getArmorPenetration() {
    return 0;
  }

  @Override
  public float getEntityHitDropChance() {
    return 0;
  }

  @Override
  public float getBlockHitDropChance() {
    return 0;
  }

  @Override
  public int getSize() {
    return 0;
  }

  @Override
  public void setSize(int size) {}

  @Override
  public void decrementSize(ItemStack magazineStack, Random random) {}

  @Override
  public ItemStack createIcon() {
    return ItemStack.EMPTY;
  }
}
