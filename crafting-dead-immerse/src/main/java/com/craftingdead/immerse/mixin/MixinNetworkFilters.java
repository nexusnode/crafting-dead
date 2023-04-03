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

package com.craftingdead.immerse.mixin;

import net.minecraft.network.Connection;
import net.minecraftforge.network.filters.NetworkFilters;
import net.minecraftforge.network.filters.VanillaPacketFilter;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.function.Function;

@Mixin(NetworkFilters.class)
public class MixinNetworkFilters {

  @Shadow(remap = false)
  @Final
  private static Map<String, Function<Connection, VanillaPacketFilter>> instances;
  @Shadow(remap = false)
  @Final
  private static Logger LOGGER;

  /**
   * @author danorris709
   * @reason Prevent error
   */
  @Overwrite(remap = false)
  public static void injectIfNecessary(Connection manager) {
    instances.forEach((key, filterFactory) -> {
      if (manager.channel().pipeline().get(key) != null) {
        return;
      }

      try {
        VanillaPacketFilter filter = filterFactory.apply(manager);
        if (((VanillaPacketFilterAccessor) filter).invokeIsNecessary(manager)) {
          manager.channel().pipeline().addBefore("packet_handler", key, filter);
          LOGGER.debug("Injected {} into {}", filter, manager);
        }
      } catch (Exception e) {

      }
    });
  }
}
