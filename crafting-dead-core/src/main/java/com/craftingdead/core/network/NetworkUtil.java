/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
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
