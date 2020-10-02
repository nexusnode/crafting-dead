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
package com.craftingdead.core.network.message.main;

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.living.ILiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetSlotMessage {

  private final int entityId;
  private final int slot;
  private final ItemStack itemStack;

  public SetSlotMessage(int entityId, int slot, ItemStack itemStack) {
    this.entityId = entityId;
    this.slot = slot;
    this.itemStack = itemStack.copy();
  }

  public static void encode(SetSlotMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeVarInt(msg.slot);
    out.writeItemStack(msg.itemStack);
  }

  public static SetSlotMessage decode(PacketBuffer in) {
    return new SetSlotMessage(in.readVarInt(), in.readVarInt(), in.readItemStack());
  }

  public static boolean handle(SetSlotMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.flatMap(w -> Optional.ofNullable(w.getEntityByID(msg.entityId)))
        .filter(e -> e instanceof LivingEntity)
        .ifPresent(e -> ILiving.get((LivingEntity) e).getItemHandler().setStackInSlot(msg.slot,
            msg.itemStack));
    return true;
  }
}
