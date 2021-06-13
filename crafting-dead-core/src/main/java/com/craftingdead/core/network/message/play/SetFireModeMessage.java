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
import com.craftingdead.core.capability.Capabilities;
import com.craftingdead.core.network.NetworkUtil;
import com.craftingdead.core.world.gun.FireMode;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetFireModeMessage {

  private final int entityId;
  private final FireMode fireMode;

  public SetFireModeMessage(int entityId, FireMode fireMode) {
    this.entityId = entityId;
    this.fireMode = fireMode;
  }

  public static void encode(SetFireModeMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeEnum(msg.fireMode);
  }

  public static SetFireModeMessage decode(PacketBuffer in) {
    return new SetFireModeMessage(in.readVarInt(), in.readEnum(FireMode.class));
  }

  public static boolean handle(SetFireModeMessage msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          ItemStack heldStack = livingEntity.getMainHandItem();
          livingEntity.getCapability(Capabilities.LIVING)
              .ifPresent(living -> heldStack
                  .getCapability(Capabilities.GUN)
                  .ifPresent(gun -> gun.setFireMode(living, msg.fireMode,
                      ctx.get().getDirection().getReceptionSide().isServer())));
        });
    return true;
  }
}
