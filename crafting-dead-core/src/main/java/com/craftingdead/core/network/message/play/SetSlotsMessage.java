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

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.living.ILiving;
import io.netty.util.collection.IntObjectHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SetSlotsMessage {

  private final int entityId;
  private final Map<Integer, ItemStack> slots;

  public SetSlotsMessage(int entityId, Map<Integer, ItemStack> slots) {
    this.entityId = entityId;
    this.slots = slots;
  }

  public static void encode(SetSlotsMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeVarInt(msg.slots.size());
    for (Map.Entry<Integer, ItemStack> entry : msg.slots.entrySet()) {
      out.writeVarInt(entry.getKey());
      out.writeItemStack(entry.getValue());
    }
  }

  public static SetSlotsMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    int slotCount = in.readVarInt();
    Map<Integer, ItemStack> slots = new IntObjectHashMap<>();
    for (int i = 0; i < slotCount; i++) {
      slots.put(in.readVarInt(), in.readItemStack());
    }
    return new SetSlotsMessage(entityId, slots);
  }

  public static boolean handle(SetSlotsMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.flatMap(w -> Optional.ofNullable(w.getEntityByID(msg.entityId)))
        .filter(e -> e instanceof LivingEntity)
        .ifPresent(e -> {
          IItemHandlerModifiable itemHandler =
              ILiving.getExpected((LivingEntity) e).getItemHandler();
          msg.slots.forEach(itemHandler::setStackInSlot);
        });
    return true;
  }
}
