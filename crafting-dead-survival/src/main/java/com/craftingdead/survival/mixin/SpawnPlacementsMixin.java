package com.craftingdead.survival.mixin;

import java.util.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.survival.CraftingDeadSurvival;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(SpawnPlacements.class)
// TODO Use SpawnPlacementRegisterEvent in 1.19+
@Deprecated(forRemoval = true)
public abstract class SpawnPlacementsMixin {

  @Inject(at = @At("RETURN"), method = "checkSpawnRules", cancellable = true)
  private static <T extends Entity> void checkSpawnRules(EntityType<T> type,
      ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, Random random,
      CallbackInfoReturnable<Boolean> callbackInfo) {
    if (type == EntityType.ZOMBIE) {
      callbackInfo.setReturnValue(CraftingDeadSurvival.checkZombieSpawnRules(
          EntityType.ZOMBIE, level, spawnType, pos, random));
    }
  }
}
