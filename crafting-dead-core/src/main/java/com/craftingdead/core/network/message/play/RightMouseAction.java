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
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.ILiving;
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class RightMouseAction {

  private final int entityId;
  private final boolean performing;

  public RightMouseAction(int entityId, boolean performing) {
    this.entityId = entityId;
    this.performing = performing;
  }

  public static void encode(RightMouseAction message, PacketBuffer out) {
    out.writeVarInt(message.entityId);
    out.writeBoolean(message.performing);
  }

  public static RightMouseAction decode(PacketBuffer in) {
    return new RightMouseAction(in.readVarInt(), in.readBoolean());
  }

  public static boolean handle(RightMouseAction message, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), message.entityId)
        .filter(e -> e instanceof LivingEntity)
        .map(e -> (LivingEntity) e)
        .<ILiving<LivingEntity, ?>>flatMap(ILiving::getOptional) // Need to cast this or else compiler complains
        .ifPresent(living -> {
          living.getEntity().getHeldItemMainhand()
              .getCapability(ModCapabilities.GUN)
              .ifPresent(gun -> gun.setPerformingRightMouseAction(living, message.performing,
                  ctx.get().getDirection().getReceptionSide().isServer()));
        });
    return true;
  }
}
