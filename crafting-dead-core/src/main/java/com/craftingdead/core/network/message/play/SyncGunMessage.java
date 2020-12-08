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

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.capability.ModCapabilities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncGunMessage {

  private final int entityId;
  private final ItemStack paintStack;
  private final ItemStack magazineStack;
  private final int magazineSize;

  public SyncGunMessage(int entityId, ItemStack paintStack, ItemStack magazineStack,
      int magazineSize) {
    this.entityId = entityId;
    this.paintStack = paintStack;
    this.magazineStack = magazineStack;
    this.magazineSize = magazineSize;
  }

  public static void encode(SyncGunMessage msg, PacketBuffer out) {
    out.writeVarInt(msg.entityId);
    out.writeItemStack(msg.paintStack);
    out.writeItemStack(msg.magazineStack);
    out.writeVarInt(msg.magazineSize);
  }

  public static SyncGunMessage decode(PacketBuffer in) {
    int entityId = in.readVarInt();
    ItemStack paintStack = in.readItemStack();
    ItemStack magazineStack = in.readItemStack();
    int size = in.readVarInt();
    return new SyncGunMessage(entityId, paintStack, magazineStack, size);
  }

  public static boolean handle(SyncGunMessage msg, Supplier<NetworkEvent.Context> ctx) {
    Optional<World> world =
        LogicalSidedProvider.CLIENTWORLD.get(ctx.get().getDirection().getReceptionSide());
    world.map(w -> w.getEntityByID(msg.entityId))
        .filter(e -> e instanceof LivingEntity)
        .map(e -> ((LivingEntity) e).getHeldItemMainhand())
        .flatMap(heldStack -> heldStack.getCapability(ModCapabilities.GUN).resolve())
        .ifPresent(gun -> {
          gun.setMagazineStack(msg.magazineStack);
          gun.setPaintStack(msg.paintStack);
          gun.setMagazineSize(msg.magazineSize);
        });
    return true;
  }
}
