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
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

public class HitMessage {

  private final Vector3d hitPos;
  private final boolean dead;

  public HitMessage(Vector3d hitPos, boolean dead) {
    this.hitPos = hitPos;
    this.dead = dead;
  }

  public static void encode(HitMessage msg, PacketBuffer out) {
    out.writeDouble(msg.hitPos.getX());
    out.writeDouble(msg.hitPos.getY());
    out.writeDouble(msg.hitPos.getZ());
    out.writeBoolean(msg.dead);
  }

  public static HitMessage decode(PacketBuffer in) {
    return new HitMessage(new Vector3d(in.readDouble(), in.readDouble(), in.readDouble()),
        in.readBoolean());
  }

  public static boolean handle(HitMessage msg, Supplier<NetworkEvent.Context> ctx) {
    if (ctx.get().getDirection().getReceptionSide().isClient()) {
      ctx.get().enqueueWork(() -> {
        ClientDist.clientConfig.hitMarkerMode.get().createHitMarker(msg.hitPos, msg.dead)
            .ifPresent(((ClientDist) CraftingDead.getInstance().getModDist())
                .getIngameGui()::displayHitMarker);
        if (msg.dead && ClientDist.clientConfig.playKillSound.get()) {
          final Minecraft minecraft = Minecraft.getInstance();
          // Plays a sound that follows the player
          minecraft.world.playMovingSound(minecraft.player, minecraft.player,
              SoundEvents.ITEM_TRIDENT_RETURN, SoundCategory.HOSTILE, 5.0F, 1.5F);
        }
      });
    }
    return true;
  }
}
