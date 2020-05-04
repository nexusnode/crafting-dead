package com.craftingdead.mod.capability.magazine;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMagazine extends INBTSerializable<CompoundNBT> {

  void hitEntity(ItemStack magazineStack, Entity entity, EntityRayTraceResult rayTrace);

  void hitBlock(ItemStack magazineStack, Entity entity, BlockRayTraceResult rayTrace);

  float getArmorPenetration();

  float getEntityHitDropChance();

  float getBlockHitDropChance();

  int getSize();

  void setSize(int size);

  void decrementSize(ItemStack magazineStack, Random random);
}
