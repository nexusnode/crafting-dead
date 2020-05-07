package com.craftingdead.mod.capability.magazine;

import java.util.Random;
import com.craftingdead.mod.capability.ModCapabilities;
import com.craftingdead.mod.capability.living.player.IPlayer;
import com.craftingdead.mod.enchantment.ModEnchantments;
import com.craftingdead.mod.item.MagazineItem;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ItemMagazine implements IMagazine {

  private final MagazineItem magazineItem;
  private int size;

  public ItemMagazine(MagazineItem magazineItem) {
    this.magazineItem = magazineItem;
    this.size = magazineItem.getSize();
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt("size", this.size);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.size = nbt.getInt("size");
  }

  @Override
  public void hitEntity(ItemStack magazineStack, Entity entity, EntityRayTraceResult rayTrace) {
    checkCreateExplosion(magazineStack, entity, rayTrace.getHitVec());

    if (EnchantmentHelper
        .getEnchantmentLevel(ModEnchantments.INCENDIARY.get(), magazineStack) > 0) {
      rayTrace.getEntity().setFire(100);
    }

    entity
        .getCapability(ModCapabilities.LIVING)
        .filter(living -> living instanceof IPlayer)
        .<IPlayer<?>>cast()
        .ifPresent(player -> player
            .infect(EnchantmentHelper
                .getEnchantmentLevel(ModEnchantments.INFECTION.get(), magazineStack) / 255.0F));
  }

  @Override
  public void hitBlock(ItemStack magazineStack, Entity entity, BlockRayTraceResult rayTrace) {
    checkCreateExplosion(magazineStack, entity, rayTrace.getHitVec());
    if (EnchantmentHelper
        .getEnchantmentLevel(ModEnchantments.INCENDIARY.get(), magazineStack) > 0) {
      World world = entity.getEntityWorld();
      BlockPos blockAbove = rayTrace.getPos().add(0, 1, 0);
      if (!world.isAirBlock(rayTrace.getPos()) && world.isAirBlock(blockAbove)) {
        world.setBlockState(blockAbove, Blocks.FIRE.getDefaultState());
      }
    }
  }

  private static void checkCreateExplosion(ItemStack magazineStack, Entity entity, Vec3d position) {
    float explosionSize =
        EnchantmentHelper.getEnchantmentLevel(ModEnchantments.EXPLOSION.get(), magazineStack)
            / 255.0F;
    if (explosionSize > 0) {
      entity
          .getEntityWorld()
          .createExplosion(entity, position.getX(), position.getY(), position.getZ(), explosionSize,
              Explosion.Mode.NONE);
    }
  }

  @Override
  public float getArmorPenetration() {
    return this.magazineItem.getArmorPenetration();
  }

  @Override
  public float getEntityHitDropChance() {
    return this.magazineItem.getEntityHitDropChance();
  }

  @Override
  public float getBlockHitDropChance() {
    return this.magazineItem.getBlockHitDropChance();
  }

  @Override
  public int getSize() {
    return this.size;
  }

  @Override
  public void setSize(int size) {
    this.size = size;
  }

  @Override
  public void decrementSize(ItemStack magazineStack, Random random) {
    final int unbreakingLevel =
        EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, magazineStack);
    if (!UnbreakingEnchantment.negateDamage(magazineStack, unbreakingLevel, random)) {
      this.size--;
    }
  }
}
