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

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.living.IPlayer;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ValidateLivingHitMessage {

  private final Map<Integer, Collection<Byte>> hits;

  public ValidateLivingHitMessage(Map<Integer, Collection<Byte>> hits) {
    this.hits = hits;
  }

  public static void encode(ValidateLivingHitMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.hits.size());
    for (Map.Entry<Integer, Collection<Byte>> hit : msg.hits.entrySet()) {
      out.writeVarInt(hit.getKey());
      out.writeVarInt(hit.getValue().size());
      for (byte value : hit.getValue()) {
        out.writeByte(value);
      }
    }
  }

  public static ValidateLivingHitMessage decode(PacketBuffer in) {
    final int hitsSize = in.readVarInt();
    Map<Integer, Collection<Byte>> hits = new Int2ObjectLinkedOpenHashMap<>();
    for (int i = 0; i < hitsSize; i++) {
      int key = in.readVarInt();
      int valueSize = in.readVarInt();
      Collection<Byte> value = new ByteArrayList();
      for (int j = 0; j < valueSize; j++) {
        value.add(in.readByte());
      }
      hits.put(key, value);
    }
    return new ValidateLivingHitMessage(hits);
  }

  public static boolean handle(ValidateLivingHitMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ServerPlayerEntity playerEntity = ctx.get().getSender();
    IPlayer<ServerPlayerEntity> player = IPlayer.getExpected(playerEntity);
    ItemStack heldStack = playerEntity.getHeldItemMainhand();
    heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
      for (Map.Entry<Integer, Collection<Byte>> hit : msg.hits.entrySet()) {
        Entity hitEntity = playerEntity.getEntityWorld().getEntityByID(hit.getKey());
        hitEntity.getCapability(ModCapabilities.LIVING).ifPresent(hitLiving -> {
          for (byte value : hit.getValue()) {
            gun.validateLivingHit(player, heldStack, hitLiving, value);
          }
        });
      }
    });
    return true;
  }
}
