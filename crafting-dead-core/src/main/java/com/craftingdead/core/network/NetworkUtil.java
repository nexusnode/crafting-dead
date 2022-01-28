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

package com.craftingdead.core.network;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;

public class NetworkUtil {

  public static Entity getEntityOrSender(NetworkEvent.Context context, int entityId) {
    return getEntityOrSender(context, entityId, Entity.class);
  }

  public static <T extends Entity> T getEntityOrSender(NetworkEvent.Context context, int entityId,
      Class<T> clazz) {
    return switch (context.getDirection().getReceptionSide()) {
      case CLIENT -> getEntity(context, entityId, clazz);
      case SERVER -> {
        if (clazz.isInstance(context.getSender())) {
          yield clazz.cast(context.getSender());
        } else {
          throw new IllegalStateException("Sender is not instance of: " + clazz.getName());
        }
      }
      default -> throw new IllegalStateException("Invalid side");
    };
  }

  public static Entity getEntity(NetworkEvent.Context context, int entityId) {
    return getEntity(context, entityId, Entity.class);
  }

  public static <T extends Entity> T getEntity(NetworkEvent.Context context, int entityId,
      Class<T> clazz) {
    return LogicalSidedProvider.CLIENTWORLD
        .get(context.getDirection().getReceptionSide())
        .map(level -> level.getEntity(entityId))
        .filter(clazz::isInstance)
        .map(clazz::cast)
        .orElseThrow(() -> new IllegalStateException(
            String.format("Entity with ID %s of type %s is absent from client level", entityId,
                clazz.getName())));
  }
}
