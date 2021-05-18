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

package com.craftingdead.core.world.item;

import java.util.Collection;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
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
    ITextComponent createTooltip(ItemStack itemStack, @Nullable World level,
        ITooltipFlag tooltipFlag);
  }
}
