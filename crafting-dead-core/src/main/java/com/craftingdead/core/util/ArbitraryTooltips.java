package com.craftingdead.core.util;

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

/**
 * Arbitrary tooltips that does not requires the implementation of vanilla's addInformation()
 * method.
 */
public class ArbitraryTooltips {

  private static final Multimap<Supplier<Item>, TooltipFunction> TO_REGISTER =
      ArrayListMultimap.create();
  private static final Multimap<Item, TooltipFunction> REGISTERED_FUNCTIONS =
      ArrayListMultimap.create();

  /**
   * Registers a function that creates an {@link ITextComponent}.
   */
  public static void registerTooltip(Supplier<Item> item, TooltipFunction function) {
    TO_REGISTER.put(item, function);
  }

  /**
   * Registers a function that creates an {@link ITextComponent}.
   */
  public static void registerTooltip(Item item, TooltipFunction function) {
    REGISTERED_FUNCTIONS.put(item, function);
  }

  /**
   * Registers every function that is waiting for register.
   */
  public static void registerAll(RegistryEvent.Register<Item> event) {
    TO_REGISTER.asMap().forEach((supplier, lines) -> {
      REGISTERED_FUNCTIONS.putAll(supplier.get(), lines);
    });
    TO_REGISTER.clear();
  }

  public static Collection<TooltipFunction> getFunctions(Item item) {
    return REGISTERED_FUNCTIONS.get(item);
  }

  @FunctionalInterface
  public static interface TooltipFunction {
    @Nullable
    public ITextComponent createTooltip(ItemStack stack, @Nullable World world,
        ITooltipFlag tooltipFlag);
  }
}
