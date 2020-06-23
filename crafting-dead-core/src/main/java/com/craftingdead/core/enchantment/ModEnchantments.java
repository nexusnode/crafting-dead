package com.craftingdead.core.enchantment;

import com.craftingdead.core.CraftingDead;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEnchantments {

  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
      new DeferredRegister<>(ForgeRegistries.ENCHANTMENTS, CraftingDead.ID);

  public static final RegistryObject<InfectionEnchantment> INFECTION = ENCHANTMENTS
      .register("infection",
          () -> new InfectionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
  public static final RegistryObject<InfectionEnchantment> ARMOR_PENETRATION = ENCHANTMENTS
      .register("armor_penetration",
          () -> new InfectionEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));
}
