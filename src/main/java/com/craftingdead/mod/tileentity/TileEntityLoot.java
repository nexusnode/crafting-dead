package com.craftingdead.mod.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLoot extends TileEntity implements ITickableTileEntity {

  public TileEntityLoot() {
    super(ModTileEntityTypes.LOOT);
  }

  private static final int DESPAWN_TIME = 2400;
  private static final int DESPAWN_DISTANCE = 50;

  private int removalTick = 0;

  @Override
  public void tick() {
    if (this.world.getClosestPlayer(this.getPos().getX(), this.getPos().getY(),
        this.getPos().getZ(), DESPAWN_DISTANCE, (entity) -> !entity.isSpectator()) == null) {
      if (this.removalTick++ >= DESPAWN_TIME) {
        if (!this.world.isRemote) {
          this.world.destroyBlock(this.getPos(), false);
          this.world.removeTileEntity(this.getPos());
        }
        this.removalTick = 0;
      }
    }
  }
}
