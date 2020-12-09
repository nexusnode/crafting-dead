package com.craftingdead.core.capability.gun;

import com.craftingdead.core.capability.animationprovider.IAnimationProvider;
import com.craftingdead.core.capability.animationprovider.gun.GunAnimationController;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public interface IGunClient extends IAnimationProvider<GunAnimationController> {

  void handleTick(ILiving<?, ?> living, ItemStack itemStack);

  void handleShoot(ILiving<?, ?> living, ItemStack itemStack);

  void handleHitEntity(ILiving<?, ?> living, ItemStack itemStack, Entity hitEntity,
      Vector3d hitPos, boolean playSound, boolean headshot);

  void handleHitBlock(ILiving<?, ?> living, ItemStack itemStack, BlockRayTraceResult rayTrace,
      boolean playSound);

  void handleToggleRightMouseAction(ILiving<?, ?> living);

  float getPartialTicks();

  boolean isFlashing();
}
