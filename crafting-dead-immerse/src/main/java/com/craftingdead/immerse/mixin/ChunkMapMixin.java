package com.craftingdead.immerse.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.craftingdead.immerse.world.level.extension.LevelExtension;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {

  @Final
  @Shadow
  private ServerLevel level;

  @Inject(method = "save", at = @At("HEAD"))
  public void save(ChunkAccess chunkAccess, CallbackInfoReturnable<Boolean> callbackInfo) {
    LevelExtension.getOrThrow(this.level).getLandManager().flush(chunkAccess.getPos());
  }
}
