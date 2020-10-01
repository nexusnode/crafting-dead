/*******************************************************************************
 * Copyright (C) 2020 Nexus Node
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
 ******************************************************************************/
package com.craftingdead.core.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.client.ClientDist;
import com.craftingdead.core.client.gui.KillFeedEntry;
import com.craftingdead.core.client.gui.KillFeedEntry.Type;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class KillFeedMessage {

  private final int playerEntityId;
  private final int deadEntityId;
  private final ItemStack weaponStack;
  private final KillFeedEntry.Type type;

  public KillFeedMessage(int playerEntityId, int deadEntityId, ItemStack weaponStack, Type type) {
    this.playerEntityId = playerEntityId;
    this.deadEntityId = deadEntityId;
    this.weaponStack = weaponStack;
    this.type = type;
  }

  public static void encode(KillFeedMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.playerEntityId);
    out.writeVarInt(msg.deadEntityId);
    out.writeItemStack(msg.weaponStack);
    out.writeEnumValue(msg.type);
  }

  public static KillFeedMessage decode(PacketBuffer in) {
    return new KillFeedMessage(in.readVarInt(), in.readVarInt(), in.readItemStack(),
        in.readEnumValue(KillFeedEntry.Type.class));
  }

  public static boolean handle(KillFeedMessage msg, Supplier<NetworkEvent.Context> ctx) {
    final Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.ifPresent(w -> {
      ctx.get().enqueueWork(() -> {
        Entity playerEntity = w.getEntityByID(msg.playerEntityId);
        Entity deadEntity = w.getEntityByID(msg.deadEntityId);
        if (playerEntity instanceof PlayerEntity && deadEntity instanceof LivingEntity) {
          ((ClientDist) CraftingDead.getInstance().getModDist()).getIngameGui()
              .addKillFeedMessage(new KillFeedEntry((PlayerEntity) playerEntity,
                  (LivingEntity) deadEntity, msg.weaponStack, msg.type));
        }
      });
    });
    return true;
  }
}
