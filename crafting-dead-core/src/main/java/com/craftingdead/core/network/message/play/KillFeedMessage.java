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

import java.util.Optional;
import java.util.function.Supplier;
import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.world.damagesource.KillFeedEntry;
import com.craftingdead.core.world.damagesource.KillFeedEntry.Type;
import net.minecraft.entity.Entity;
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

  public KillFeedMessage(KillFeedEntry entry) {
    this(entry.getKillerEntity().getId(), entry.getDeadEntity().getId(),
        entry.getWeaponStack(), entry.getType());
  }

  public KillFeedMessage(int playerEntityId, int deadEntityId, ItemStack weaponStack, Type type) {
    this.playerEntityId = playerEntityId;
    this.deadEntityId = deadEntityId;
    this.weaponStack = weaponStack;
    this.type = type;
  }

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.playerEntityId);
    out.writeVarInt(this.deadEntityId);
    out.writeItem(this.weaponStack);
    out.writeEnum(this.type);
  }

  public static KillFeedMessage decode(PacketBuffer in) {
    return new KillFeedMessage(in.readVarInt(), in.readVarInt(), in.readItem(),
        in.readEnum(KillFeedEntry.Type.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> LogicalSidedProvider.CLIENTWORLD
        .<Optional<World>>get(ctx.get().getDirection().getReceptionSide())
        .ifPresent(level -> {
          Entity playerEntity = level.getEntity(this.playerEntityId);
          Entity deadEntity = level.getEntity(this.deadEntityId);
          if (playerEntity instanceof PlayerEntity && deadEntity instanceof PlayerEntity) {
            CraftingDead.getInstance().getClientDist().getIngameGui()
                .addKillFeedMessage(new KillFeedEntry((PlayerEntity) playerEntity,
                    (PlayerEntity) deadEntity, this.weaponStack, this.type));
          }
        }));
    return true;
  }
}
