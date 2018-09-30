package com.craftingdead.mod.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityLoot extends TileEntity implements ITickable {

	private static final int DESPAWN_TIME = 2400;
	private static final int DESPAWN_DISTANCE = 50;

	private int removalTick = 0;

	@Override
	public void update() {
		if (this.world.getClosestPlayer(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(),
				DESPAWN_DISTANCE, false) == null) {
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