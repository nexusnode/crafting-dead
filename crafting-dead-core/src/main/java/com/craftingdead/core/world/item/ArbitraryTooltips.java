/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.item;

import java.util.Collection;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IRegistryDelegate;

/**
 * Arbitrary tooltips that does not requires the implementation of vanilla's addInformation()
 * method.
 */
public class ArbitraryTooltips {

  private static final Multimap<Supplier<Item>, TooltipFunction> toRegister =
      ArrayListMultimap.create();
  private static final Multimap<IRegistryDelegate<Item>, TooltipFunction> functions =
      ArrayListMultimap.create();

  public static void registerTooltip(Supplier<Item> item, TooltipFunction function) {
    toRegister.put(item, function);
  }

  public static void registerTooltip(Item item, TooltipFunction function) {
    functions.put(item.delegate, function);
  }

  public static void registerAll(RegistryEvent.Register<Item> event) {
    toRegister.asMap()
        .forEach((supplier, lines) -> functions.putAll(supplier.get().delegate, lines));
    toRegister.clear();
  }

  public static Collection<TooltipFunction> getFunctions(Item item) {
    return functions.get(item.delegate);
  }

  @FunctionalInterface
  public interface TooltipFunction {

    @Nullable
    Component createTooltip(ItemStack itemStack, @Nullable Level level,
        TooltipFlag tooltipFlag);
  }
}
