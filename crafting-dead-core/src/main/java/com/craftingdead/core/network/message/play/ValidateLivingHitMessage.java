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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ValidateLivingHitMessage {

  private final Map<Long, Integer> hits;

  public ValidateLivingHitMessage(Map<Long, Integer> hits) {
    this.hits = hits;
  }

  public static void encode(ValidateLivingHitMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.hits.size());
    for (Map.Entry<Long, Integer> hit : msg.hits.entrySet()) {
      out.writeVarLong(hit.getKey());
      out.writeVarInt(hit.getValue());
    }
  }

  public static ValidateLivingHitMessage decode(PacketBuffer in) {
    final int hitsSize = in.readVarInt();
    Map<Long, Integer> hits = new HashMap<>();
    for (int i = 0; i < hitsSize; i++) {
      hits.put(in.readVarLong(), in.readVarInt());
    }
    return new ValidateLivingHitMessage(hits);
  }

  public static boolean handle(ValidateLivingHitMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ServerPlayerEntity playerEntity = ctx.get().getSender();
    playerEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      ItemStack heldStack = playerEntity.getHeldItemMainhand();
      heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
        for (Map.Entry<Long, Integer> hit : msg.hits.entrySet()) {
          Entity hitEntity = playerEntity.getEntityWorld().getEntityByID(hit.getValue());
          hitEntity.getCapability(ModCapabilities.LIVING).ifPresent(hitLiving -> {
            gun.validateLivingHit(living, heldStack, hitLiving, hit.getKey());
          });
        }
      });
    });
    return true;
  }
}
