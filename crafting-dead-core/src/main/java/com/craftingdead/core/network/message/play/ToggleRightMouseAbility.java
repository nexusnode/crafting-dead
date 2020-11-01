/**
 * Crafting Dead
 * Copyright (C) 2020  Nexus Node
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
import com.craftingdead.core.network.util.NetworkUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ToggleRightMouseAbility {

  private final int entityId;

  public ToggleRightMouseAbility(int entityId) {
    this.entityId = entityId;
  }

  public static void encode(ToggleRightMouseAbility msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
  }

  public static ToggleRightMouseAbility decode(PacketBuffer in) {
    return new ToggleRightMouseAbility(in.readVarInt());
  }

  public static boolean handle(ToggleRightMouseAbility msg, Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil
        .getEntity(ctx.get(), msg.entityId)
        .filter(entity -> entity instanceof LivingEntity)
        .ifPresent(entity -> {
          LivingEntity livingEntity = (LivingEntity) entity;
          ItemStack heldStack = livingEntity.getHeldItemMainhand();
          livingEntity.getCapability(ModCapabilities.LIVING)
              .ifPresent(living -> heldStack
                  .getCapability(ModCapabilities.GUN)
                  .ifPresent(gun -> gun
                      .toggleRightMouseAction(living,
                          ctx.get().getDirection().getReceptionSide().isServer())));
        });
    return true;
  }
}
