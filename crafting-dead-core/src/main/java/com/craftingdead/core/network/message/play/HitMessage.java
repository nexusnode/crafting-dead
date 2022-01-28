/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.craftingdead.core.network.message.play;

import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public class HitMessage {

  private final Vec3 hitPos;
  private final boolean dead;

  public HitMessage(Vec3 hitPos, boolean dead) {
    this.hitPos = hitPos;
    this.dead = dead;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeDouble(this.hitPos.x());
    out.writeDouble(this.hitPos.y());
    out.writeDouble(this.hitPos.z());
    out.writeBoolean(this.dead);
  }

  public static HitMessage decode(FriendlyByteBuf in) {
    return new HitMessage(new Vec3(in.readDouble(), in.readDouble(), in.readDouble()),
        in.readBoolean());
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(
        () -> CraftingDead.getInstance().getClientDist().handleHit(this.hitPos, this.dead));
    return true;
  }
}
