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

import com.craftingdead.core.capability.ModCapabilities;
import com.craftingdead.core.capability.gun.Hit;
import com.craftingdead.core.util.BufUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ValidateLivingHitMessage {

  private final List<Hit> hits;

  public ValidateLivingHitMessage(List<Hit> hits) {
    this.hits = hits;
  }

  public static void encode(ValidateLivingHitMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.hits.size());
    ;
    for (Hit hit : msg.hits) {
      out.writeVarLong(hit.getGameTime());
      out.writeVarInt(hit.getEntityId());
      BufUtil.writeVec(out, hit.getHitVec());
    }
  }

  public static ValidateLivingHitMessage decode(PacketBuffer in) {
    final int hitsSize = in.readVarInt();
    List<Hit> hits = new ArrayList<>();
    for (int i = 0; i < hitsSize; i++) {
      long gameTime = in.readVarLong();
      int entityId = in.readVarInt();
      Vec3d hitVec = BufUtil.readVec(in);
      Hit hit = new Hit(gameTime, entityId, hitVec);
      hits.add(hit);
    }
    return new ValidateLivingHitMessage(hits);
  }

  public static boolean handle(ValidateLivingHitMessage msg, Supplier<NetworkEvent.Context> ctx) {
    ServerPlayerEntity playerEntity = ctx.get().getSender();
    playerEntity.getCapability(ModCapabilities.LIVING).ifPresent(living -> {
      ItemStack heldStack = playerEntity.getHeldItemMainhand();
      heldStack.getCapability(ModCapabilities.GUN).ifPresent(gun -> {
        for (Hit hit : msg.hits) {
          Entity hitEntity = playerEntity.getEntityWorld().getEntityByID(hit.getEntityId());
          hitEntity.getCapability(ModCapabilities.LIVING).ifPresent(hitLiving -> {
            gun.validateLivingHit(living, heldStack, hitLiving, hit);
          });
        }
      });
    });
    return true;
  }
}
