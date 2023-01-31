package com.craftingdead.survival.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.survival.world.entity.extension.ZombieHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {

  protected ZombieMixin(EntityType<? extends Monster> type, Level level) {
    super(type, level);
  }

  @Override
  public float getWalkTargetValue(BlockPos pos, LevelReader level) {
    return 0.0F;
  }

  @Inject(at = @At("RETURN"), method = "setBaby")
  public void setBaby(boolean baby, CallbackInfo callbackInfo) {
    var zombie = (Zombie) (Object) this;
    zombie.getCapability(LivingExtension.CAPABILITY).resolve()
        .flatMap(extension -> extension.getHandler(ZombieHandler.TYPE))
        .ifPresent(handler -> handler.handleSetBaby(baby));
  }

  @Inject(at = @At("RETURN"), method = "isSunSensitive", cancellable = true)
  public void isSunSensitive(CallbackInfoReturnable<Boolean> callbackInfo) {
    callbackInfo.setReturnValue(false);
  }

  @Inject(at = @At("RETURN"), method = "populateDefaultEquipmentSlots")
  public void populateDefaultEquipmentSlots(DifficultyInstance difficulty,
      CallbackInfo callbackInfo) {
    var zombie = (Zombie) (Object) this;
    zombie.getCapability(LivingExtension.CAPABILITY).resolve()
        .flatMap(extension -> extension.getHandler(ZombieHandler.TYPE))
        .ifPresent(handler -> handler.populateDefaultEquipmentSlots(difficulty));
  }
}
